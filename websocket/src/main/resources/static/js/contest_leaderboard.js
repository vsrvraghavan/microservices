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

var company_panel_label = document.getElementById('company_panel_label');


var stompClient = null;
var username = null;

var companyname = null;

var contestname = null;

var xmlhttp;

var contest_data = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];



var cardCount = 0;



function addCard(lb_id, lb_name, lb_data, panel_id){
	
	cardCount = cardCount+1;		
    var myCol = $('<div class="col-sm-3 col-md-3 pb-2"></div>');
    var myPanel = $('<div class="card card-outline-info panelCard" id="'+lb_id+'"><div class="card-block" style="margin-left:2px"><div class="card-title"><span>'+lb_name+'</span><button type="button" class="close close_btn" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right float-right-button"><i class="fa"></i></span></button></div><p>   ' +lb_data + ' </p></div></div>');
    //    var myPanel = $('<div class="card card-outline-info panelCard" id="'+lb_id+'"><div class="card-block" style="margin-left:2px"><div class="card-title"><span>'+lb_name+'</span><button type="button" class="close" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right float-right-button"><i class="fa fa-remove"></i></span></button></div><p>   ' +lb_data + ' </p></div></div>');
    myPanel.appendTo(myCol);
    myCol.appendTo(panel_id);
    
	$('.close').on('click', function(e){
	  e.stopPropagation();  
	      var $target = $(this).parents('.col-sm-3');
	      $target.hide('slow', function(){ $target.remove(); });
	});
};




function connect(event) {
    username = document.querySelector('#name').value.trim();
    companyname = document.querySelector('#companyname').value.trim();
    contestname = document.querySelector('#contestname').value.trim();

    if(username && companyname) {

        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        /////company_panel_label.innerHTML += " "+companyname;
//        var socket = new SockJS('http://localhost:9000/ws');
//        var socket = new SockJS('http://localhost:9000/dashqrapi/ws');        
        var socket = new SockJS('http://www.swan-speed.com:9000/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Channel
    stompClient.subscribe('/channel/public/'+ companyname+"/"+ contestname, onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/leaderboard.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
	getPanelData();
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

        stompClient.send("/app/leaderboard.sendMessage", {}, JSON.stringify(adminMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log("on onMessageReceived");
    console.log(message);
    if(message.type === 'JOIN') {              
        // Call web service to get all panel data after connecting to socket server
    		//getPanelData();
    }else if(message.type === 'DATA') { 
    		console.log("message type is data");
    	    console.log(message.content); 
    	    setPanelCard(message.content);
    	    /////setPanelData(message.content, message.messageFor);
    }   
}



function setPanelCard(messageContent){
	console.log("in  setPanelCard");
	
	contest_data = messageContent.contestLB;
	var contest_name = messageContent.contestName;
	
	
	console.log("contest_lb "+ JSON.stringify(contest_data));
	var contest_lb = contest_data.lb;
	var playerRankData = ""; 
	for(var data in contest_lb){
		var name = contest_lb[data].name;
		var score = contest_lb[data].score;		
		playerRankData += "<div class=\"player_container\"><div>"+name+"</div><div>:</div><div>"+ score +"</div></div>";
	}
	
	
	var scoreboardElement =  document.getElementById("contest_"+contest_name+"_lb");		 	
	if (typeof(scoreboardElement) != 'undefined' && scoreboardElement != null){
		var lb_id ="contest_"+contest_name+"_lb";
		scoreboardElement.innerHTML = '<div class="card-block"><div class="card-title"><span>Contest : '+contest_name+'</span><button type="button" class="close" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right"><i class="fa fa-remove"></i></span></button></div><p>   ' +playerRankData + ' </p></div>';
	}else{		
		addCard("contest_"+contest_name+"_lb" , "Contest : "+contest_name , playerRankData, '#contest_Panel');
	}
		
	
	playerRankData = "";
	
	var contest_game_lb = contest_data.gameLB;
	
	for(var data in contest_game_lb){
		var game_name = contest_game_lb[data].gameID;//data.gameID;
		console.log("contest_game_lb - game_name : " + game_name);
		var game_data = contest_game_lb[data].lb;	
		if(game_data.length > 0){
			for(var game_lb in game_data){
				var name = game_data[game_lb].name;
				var score = game_data[game_lb].score;
				playerRankData += "<div class=\"player_container\"><div>"+name+"</div><div>:</div><div>"+ score +"</div></div>";
			}
			var lb_id = "contest_"+contest_name+"_game_"+game_name+"_lb";
			var gamescoreboardElement =  document.getElementById(lb_id);		 	
			if (typeof(gamescoreboardElement) != 'undefined' && gamescoreboardElement != null){		
				
				gamescoreboardElement.innerHTML = '<div class="card-block"><div class="card-title"><span>Game : '+game_name+'</span><button type="button" class="close" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right"><i class="fa fa-remove"></i></span></button></div><p>   ' +playerRankData + ' </p></div>';
			}else{		
				addCard(lb_id , "Game : "+game_name , playerRankData, '#contest_Panel');				
			}
			playerRankData = "";
			
		}
		
	}
	
	
	var department_lb_data = contest_data.departmentLB;
	console.log("department_lb "+ JSON.stringify(department_lb_data));
	for(var department_lb in department_lb_data){
		
		var department_name = department_lb_data[department_lb].departmentID;	
		console.log("department_name : "+ department_name);	
		var contest_department_lb = department_lb_data[department_lb].lb;
		console.log("contest_department_lb : "+ JSON.stringify(contest_department_lb));

		var playerRankData = ""; 
		for(var data in contest_department_lb){
			var name = contest_department_lb[data].name;
			var score = contest_department_lb[data].score;		
			playerRankData += "<div class=\"player_container\"><div>"+name+"</div><div>:</div><div>"+ score +"</div></div>";
		}
		
		var scoreboardElement =  document.getElementById("contest_"+contest_name+"_department_"+department_name+"_lb");		 	
		if (typeof(scoreboardElement) != 'undefined' && scoreboardElement != null){
			var lb_id ="contest_"+contest_name+"_department_"+department_name+"_lb";
			scoreboardElement.innerHTML = '<div class="card-block"><div class="card-title"><span>Department : '+department_name+'</span><button type="button" class="close" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right"><i class="fa fa-remove"></i></span></button></div><p>   ' +playerRankData + ' </p></div>';
		}else{		
			addCard("contest_"+contest_name+"_department_"+department_name+"_lb" , "Department : "+department_name , playerRankData, '#department_contest_Panel');
		}
		
		
		//addCard(department_name);
	}
	//curl -X POST --data '{"companyName":"Swanspeed","contestID":"101","contestStatus":"STARTED","gamesAllowed":["GAME1","GAME2"],"locationsAndDepartments":[{"location":"New York","department":["HR","ENG","ADMIN"]}]}' -H "Content-Type: application/json" http://34.218.108.31:8181/contestleaderBoard
	//curl -X POST --data '{"companyName":"1Huddle","contestID":"101","contestStatus":"CONTINUING","playersAndPoints":[{"registeredPlayer":{"companyID":"1Huddle","gameID":"GAME1","locationID":"New York","departmentID":"HR","playerID":"ragha_AT_swanspeed.com"},"contestTicket":"Contest-101-1001","pointsEarned":10}]}' -H "Content-Type: application/json" http://34.218.108.31:8181/contestleaderBoard
	//curl -X POST --data '{"companyName":"Swanspeed","contestID":"Contest-01","contestStatus":"CONTINUING","playersAndPoints":[{"registeredPlayer":{"companyID":"Swanspeed","gameID":"GAME2","locationID":"Pune","departmentID":"DEV","playerID":"ANDY_AT_Swanspeed.com"},"contestTicket":"Contest-101-1001","pointsEarned":30}]}' -H "Content-Type: application/json" http://34.218.108.31:8181/contestleaderBoard
	var location_lb_data = contest_data.locationLB;
		console.log("location_lb_data "+ JSON.stringify(location_lb_data));
	for(var location_lb in location_lb_data){
		console.log("Data : " + JSON.stringify(location_lb_data[location_lb]));
		var location_name = location_lb_data[location_lb].locationID;	
		console.log("location_name : "+ location_name);	
		var contest_location_lb = location_lb_data[location_lb].lb;
		console.log("contest_location_lb : "+ JSON.stringify(contest_location_lb));

		var playerRankData = ""; 
		for(var data in contest_location_lb){
			var name = contest_location_lb[data].name;
			var score = contest_location_lb[data].score;		
			playerRankData += "<div class=\"player_container\"><div>"+name+"</div><div>:</div><div>"+ score +"</div></div>";
		}
		var lb_id ="contest_"+contest_name+"_location_"+location_name+"_lb";
		var scoreboardElement =  document.getElementById(lb_id);		 	
		if (typeof(scoreboardElement) != 'undefined' && scoreboardElement != null){			
			scoreboardElement.innerHTML = '<div class="card-block"><div class="card-title"><span>Location : '+location_name+'</span><button type="button" class="close" data-target="#'+lb_id+'" data-dismiss="alert"><span class="float-right"><i class="fa fa-remove"></i></span></button></div><p>   ' +playerRankData + ' </p></div>';
		}else{		
			addCard(lb_id , "Location : "+location_name , playerRankData, '#location_contest_Panel');
		}
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
	          setPanelCard(jsondata);
	        }
	        else{
	          alert("An error has occured making the request");
	        }
	   }
	  }
	 //var url ='http://localhost:9000/getpaneldata';
	 var url ='http://www.swan-speed.com:9000/getcontestdashoard?companyid='+companyname+'&contestid='+contestname;
	 
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

	
	if(messageObject.leaderBoardRankData != null){
		for (var i in messageObject.leaderBoardRankData) {				
			var keys = [];
			Object.entries(messageObject.leaderBoardRankData[i]).forEach(function([key, value]){
		        if(keys.indexOf(key) == -1)
		        {
		        	var scoreboardElement =  document.getElementById(key);		 
		        	scoreboardElement.innerHTML = "";
		        	if (typeof(scoreboardElement) != 'undefined' && scoreboardElement != null)
		        	{
		        		for (var i in value) {
		        			var lbObject = value[i];
		        			var playerRankData = "<div class=\"player_container\"><div>"+lbObject.member +"</div><div>:</div><div>"+lbObject.score +"</div></div>";
		        			scoreboardElement.innerHTML += playerRankData;
		        		}		        		
		        	}		        	
		        }
		    });			
		}				
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
//messageForm.addEventListener('submit', sendMessage, true)


