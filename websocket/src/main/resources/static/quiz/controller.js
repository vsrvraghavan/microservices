$(document).ready(function () {
	
	var questionNumber=0;
	var questionBank=new Array();
	var stage="#game1";
	var stage2=new Object;
	var questionLock=false;
	var numberOfQuestions;
	var numnerOfCorrectAnswers=0;
	var score=0;
	var data = {};
	var userId;
	var companyId;
	var contestId;
	var gameId;
	var locationId;
	var departmentId;

	var contestSessionId;
	var xhr = new XMLHttpRequest();


	var contest_server_name = "34.218.108.31";
	var contest_server_port = "9090";

	var quiz_server_name = "34.218.108.31";	
	var quiz_server_port = "8999";	

	var randomString = function (length) {
		var chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
		var result = '';
		for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
		return result;
	}	

	var hideElement = function(val){
		$("#"+val).css("visibility", "hidden");
		$("#"+val).css("display", "none");
	}

	var showElement = function(val){
		$("#"+val).css("visibility", "visible");
		$("#"+val).css("display", "block");
	}
	
	$('#loginBtn').on('click', function(event) {
		hideElement("loginContainer");
		showElement("startGameContainer");	
		userId = $("#userId").val();
		companyId = $("#companyId").val();
		locationId = $("#locationId").val();
		departmentId = 	$("#departmentId").val();
		console.log(userId + "  " + companyId + "  " + locationId + "  " + departmentId)
	});

	$('#startBtn').on('click', function(event) {
		hideElement("startGameContainer");
		showElement("playGameContainer");
		contestId =	$("#contestId").val();
		gameId =	$("#gameId").val();	
		console.log(userId + "  " + companyId + "  " + locationId + "  " + departmentId + "  " + contestId + "  "+ gameId)
		var url = "http://"+contest_server_name+":"+contest_server_port+"/ticket";
		var requestData = JSON.stringify({"sessionIDAtGateway":randomString(4),"companyName":companyId,"contestID":contestId,"fromLocation":departmentId,"fromDepartment":departmentId,"playerID":userId,"gameID":gameId});
		postData(url, requestData, processJoinContestGameResponse);			
	});
	
	$('#playGameBtn').on('click', function(event) {
		hideElement("playGameContainer");
		showElement("QuizContainer");
		
		url = "http://"+contest_server_name+":"+contest_server_port+"/start";
		requestData = JSON.stringify({"companyID":companyId,"departmentID":departmentId,"gameID":gameId,"playerID":userId,"gameType":"SP","gameName":gameId,"gameSessionUUID":contestSessionId,"playedInTimezone":"Asia/Kolkata"});
		postData(url, requestData, processStartGameResponse);		
	});
	
	var processStartGameResponse = function(){
		if (xhr.readyState==4 && xhr.status==200) {
        	var data = JSON.parse(xhr.responseText);
			console.log(data);
			if(data.opSuccess){
				var url = "http://"+contest_server_name+":"+contest_server_port+"/prepare";	
				requestData = JSON.stringify({"sessionID":contestSessionId,"questionMetadata":"Not of any use right now"});
				postData(url, requestData, processPrepareGameResponse);	
			}			
		}
	}
	
	
	var processPrepareGameResponse = function(){
		
		if (xhr.readyState==4 && xhr.status==200) {
        	var data = JSON.parse(xhr.responseText);
			console.log(data);
			if(data.opSuccess){
				var url = "http://"+quiz_server_name+":"+quiz_server_port+"/tfType";
				var requestData = JSON.stringify({"gameID":gameId,"gameCategory":"Soccer","prefLang":"en_us","questionType":"TFType","countOfQues":3,"optionsPerAnswer":2});
				postData(url, requestData, processQuestionResponse);
			}			
		}
		
	}
	
	var submitSingleQuestion = function(){
	
	//Contest-1Huddle-102-0001
	
	
	}
	
			
	var processQuestionResponse = function(){
				
		if (xhr.readyState==4 && xhr.status==200) {
        	var data = JSON.parse(xhr.responseText);
			console.log(data);
			var gameId = data.gameID;
			var gameCategory = data.gameCategory;
			var prefLang = data.prefLang;

			for(var i=0;i<data.quesBoard.length;i++){ 
				questionBank[i]=new Array;
				questionBank[i][0]=data.quesBoard[i].q;
				questionBank[i][1]=data.quesBoard[i].a[0].humanReadableAnswerText;
				questionBank[i][2]=data.quesBoard[i].a[1].humanReadableAnswerText;
			}
			numberOfQuestions=questionBank.length; 				 
			displayQuestion();
    	}	
	}
	
	var processJoinContestGameResponse = function(){		
		if (xhr.readyState==4 && xhr.status==200) {
        	data = JSON.parse(xhr.responseText);			
			if(data.opSuccess){
				console.log(contestSessionId);
				contestSessionId = data.contents.ticket;			
			}		
    	}
    		
	}	

/// need to implement 	

	var postAnswerResponse = function(){		
		if (xhr.readyState==4 && xhr.status==200) {
        	data = JSON.parse(xhr.responseText);			
			if(data.opSuccess){
				
				
			}		
    	}
    		
	}
	
	var getGameScoreResponse = function(){		
		if (xhr.readyState==4 && xhr.status==200) {
        	data = JSON.parse(xhr.responseText);			
			if(data.opSuccess){
				$(stage).append('<div class="questionText">You have finished the quiz!<br><br>Total questions: '+numberOfQuestions+'<br>Correct answers: '+numnerOfCorrectAnswers+'</div>');						
			}		
    	}
    		
	}		

	
	var postData = function(url, payload, callback){				
	  	xhr = new XMLHttpRequest();
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type", "application/json");	
		xhr.onreadystatechange = function() {
		  callback();
		};
		xhr.send(payload);			
	}
	
	
	
	function displayQuestion(){
		 var rnd=Math.random()*2;
		 rnd=Math.ceil(rnd);
		 var a1;
		 var a2;
		 var a1Iscorrect;
		 var a2Iscorrect;
		// var q3;
		//  alert(rnd);
		//if(rnd==1){a1=questionBank[questionNumber][1];a2=questionBank[questionNumber][2];}
		//if(rnd==2){a2=questionBank[questionNumber][1];a1=questionBank[questionNumber][2];}
		a1=questionBank[questionNumber][1];
		a2=questionBank[questionNumber][2];
		//$(stage).append('<div class="questionText">'+questionBank[questionNumber][0]+'</div><div id="1" class="option">'+q1+'</div><div id="2" class="option">'+q2+'</div>');

		$(stage).append('<div class="questionText">'+questionBank[questionNumber][0]+'</div><div id="1" class="option">'+a1+'</div><div id="2" class="option">'+a2+'</div>');

		$('.option').click(function(){
			if(questionLock==false){questionLock=true;	
				//correct answer
				var anspos = (this.id -1);
				var isCorrect = false;
				if(data.quesBoard[questionNumber].a[anspos].isCorrect == 'Y'){
				isCorrect = true;
			}			  
						  
			if(isCorrect){
				$(stage).append('<div class="feedback1">CORRECT</div>');
				numnerOfCorrectAnswers++;
				score = score + 100;
			}else{
				$(stage).append('<div class="feedback2">WRONG</div>');			  
			}
			var url = "http://"+contest_server_name+":"+contest_server_port+"/play";	
			requestData = JSON.stringify({"sessionID":contestSessionId,"questionID":questionNumber,"answerID":anspos,"isCorrect":isCorrect,"points":score,"timeSpentToAnswerAtFE":1});								  
			postData(url, requestData, postAnswerResponse);
				  /*
				  if(data.quesBoard[questionNumber].a[anspos].isCorrect == 'Y'){
				   $(stage).append('<div class="feedback1">CORRECT</div>');
				   numnerOfCorrectAnswers++;
				   }
				  //wrong answer	
				  if(data.quesBoard[questionNumber].a[anspos].isCorrect == 'N'){
				   $(stage).append('<div class="feedback2">WRONG</div>');
				  }
				  */
			setTimeout(function(){changeQuestion()},1000);
			 }
		})
	}//display question

	
	function changeQuestion(){		
		questionNumber++;	
		if(stage=="#game1"){
			stage2="#game1";stage="#game2";
		}else{
			stage2="#game2";stage="#game1";
		}	
		if(questionNumber<numberOfQuestions){
			displayQuestion();
		}else{
			gameEnd();
		}	
		$(stage2).animate({"right": "+=800px"},"slow", function() {$(stage2).css('right','-800px');$(stage2).empty();});
		$(stage).animate({"right": "+=800px"},"slow", function() {questionLock=false;});
	}//change question
	

	
	
	function gameEnd(){		
		var url = "http://"+contest_server_name+":"+contest_server_port+"/end";	
		requestData = JSON.stringify({"sessionID":contestSessionId,"totalTimeTakenByPlayerAtFE":10});								  
		postData(url, requestData, getGameScoreResponse);		
	}//display final slide
	
	
});//doc ready