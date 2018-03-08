'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');


var panel1 = document.getElementById('panel1');
var panel2 = document.getElementById('panel2');
var panel3 = document.getElementById('panel3');
var panel4 = document.getElementById('panel4');
var panel5 = document.getElementById('panel5');
var panel6 = document.getElementById('panel6');
var panel7 = document.getElementById('panel7');
var panel8 = document.getElementById('panel8');

var stompClient = null;
var username = null;

var xmlhttp;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {

        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Channel
    stompClient.subscribe('/channel/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    //connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var adminMessage = {
            content: messageInput.value,
            type: 'DATA'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(adminMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.type === 'JOIN') {              
        // Call web service to get all panel data after connecting to socket server
    		getPanelData();
    }else if(message.type === 'DATA') { 
    	    console.log(message.content);    
    	    setPanelData(message.content, message.messageFor);
    }   
}

function getPanelData(){
	xmlhttp = new XMLHttpRequest();	 
	 xmlhttp.onreadystatechange = function() {
	 if (xmlhttp.readyState==4){
	        if (xmlhttp.status==200 || window.location.href.indexOf("http")==-1){
	        	console.log('xmlhttp.responseText');
	        	console.log(xmlhttp.responseText);	        	  
	          var jsondata=eval("("+xmlhttp.responseText+")") //retrieve result as an JavaScript object
	          console.log('jsondata');
	          console.log(jsondata);          
			  setPanelData(jsondata.content, 'all');
	        }
	        else{
	          alert("An error has occured making the request");
	        }
	   }
	  }
	 //var url ='http://localhost:9000/getpaneldata';
	 var url ='http://localhost:9000/getpaneldata';
	 
	 xmlhttp.open('GET',url,true);
     xmlhttp.send(null);     
	
}



function setPanelData(messageObject, messageFor){
	
	//var messageObject = message.content;
	
	if(messageObject.gameSessionsLaunched != null){
		panel1.innerHTML = messageObject.gameSessionsLaunched;
	}else{
		panel1.innerHTML = "0";
	}
	
	if(messageObject.gameSessionsFinishedByPlayer != null){
		panel2.innerHTML = messageObject.gameSessionsFinishedByPlayer;
	}else{
		panel2.innerHTML = "0";
	}
	
	if(messageObject.gameSessionsFinishedByManager != null){
		panel3.innerHTML = messageObject.gameSessionsFinishedByManager;
	}else{
		panel3.innerHTML = "0";
	}
	
	if(messageObject.gameSessionsFinishedByTimeout != null){
		panel4.innerHTML = messageObject.gameSessionsFinishedByTimeout;
	}	else{
		panel4.innerHTML = "0";
	}
	
	
	if(messageObject.lb1 != null){
		panel5.innerHTML = "";
		for (var i in messageObject.lb1) {
			var lbObject = messageObject.lb1[i];
			var playerRankData = "<div class=\"player_container\"><div>"+lbObject.member +"</div><div>:</div><div>"+lbObject.score +"</div></div>";
			panel5.innerHTML += playerRankData;
		}
		
		//.innerHTML = messageObject.leaderDataGame1;
	}	else{
		//panel5.innerHTML = "&nbsp;";
	}
	
	if(messageObject.lb2 != null){
		panel6.innerHTML = "";
		for (var i in messageObject.lb2) {
			var lbObject = messageObject.lb2[i];
			var playerRankData = "<div class=\"player_container\"><div>"+lbObject.member +"</div><div>:</div><div>"+lbObject.score +"</div></div>";
			panel6.innerHTML += playerRankData;
		}
	}	else{
		//panel6.innerHTML = "&nbsp;";
	}
	
	if(messageObject.lbC != null){
		panel7.innerHTML = "";
		for (var i in messageObject.lbC) {
			var lbObject = messageObject.lbC[i];
			var playerRankData = "<div class=\"player_container\"><div>"+lbObject.member +"</div><div>:</div><div>"+lbObject.score +"</div></div>";
			panel7.innerHTML += playerRankData;
		}
	}	else{
		//panel7.innerHTML = "&nbsp;";
	}
	
	if(messageObject.lbD != null){
		panel8.innerHTML = "";
		for (var i in messageObject.lbD) {
			var lbObject = messageObject.lbD[i];
			var playerRankData = "<div class=\"player_container\"><div>"+lbObject.member +"</div><div>:</div><div>"+lbObject.score +"</div></div>";
			panel8.innerHTML += playerRankData;
		}
	}	else{
		//panel8.innerHTML = "&nbsp;";
	}

}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)