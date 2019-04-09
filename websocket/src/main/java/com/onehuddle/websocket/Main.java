package com.onehuddle.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Hello world!
 *
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onehuddle.commons.pojo.AdminPanelMessage;
import com.onehuddle.commons.pojo.LeaderData;
import com.onehuddle.commons.pojo.AdminPanelMessageData;
import com.onehuddle.commons.pojo.ContestLeaderboardMessage;
import com.onehuddle.commons.pojo.AdminPanelMessage.AdminPanelMessageType;
import com.onehuddle.websocket.utils.Httputil;


@SpringBootApplication
@RestController
@EnableAutoConfiguration
@Controller
@CrossOrigin(origins = {"*"},
maxAge = 4800, allowCredentials = "false")
public class Main {
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	private static String gameSessionLaunched = "0";	
	private static String gameSessionFinishedByPlayer = "0";	
	private static String gameSessionFinishedByManager = "0";
	private static String gameSessionFinishedByTimeout = "0";
	private static List<LeaderData> game_1_data = null;
	private static List<LeaderData> game_2_data = null;
	
	private static List<LeaderData> game_3_data = null;
	private static List<LeaderData> game_4_data = null;
	
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	/*
	
	@RequestMapping(value="/adminpanel/{companyId}", method = RequestMethod.POST)
    public String pushPanelData(@PathVariable String companyId,  @RequestBody AdminPanelMessage data) {

		System.out.println("In adminpanel POST");
		System.out.println(data);		
		//this.webSocket.convertAndSend("/app/chat.sendMessage/"+ companyId, setAdminPanelMessage(data));
		
		this.webSocket.convertAndSend("/channel/public/"+ companyId, setAdminPanelMessage(data));
		
		return String.valueOf("posted");
		
    }
	
	*/
	
	@RequestMapping(value="/adminpanel/{companyId}/{contestId}", method = RequestMethod.POST)
    public String pushPanelData(@PathVariable String companyId,  @PathVariable String contestId, @RequestBody ContestLeaderboardMessage data) {

		System.out.println("In adminpanel POST");
		System.out.println(data);		
		//this.webSocket.convertAndSend("/app/chat.sendMessage/"+ companyId, setAdminPanelMessage(data));
		
		//this.webSocket.convertAndSend("/channel/public/"+ companyId+"/", setAdminPanelMessage(data));
				
		this.webSocket.convertAndSend("/channel/public/"+ companyId+"/"+contestId, data);
		
		return String.valueOf("posted");
		
    }
	
	
	/*
	private AdminPanelMessage setAdminPanelMessage(AdminPanelMessage data) {
		

		if(data.getContent().getGameSessionsLaunched() != null) {
			gameSessionLaunched = data.getContent().getGameSessionsLaunched();
		}else {
			data.getContent().setGameSessionsLaunched(gameSessionLaunched);
		}
		
		if(data.getContent().getGameSessionsFinishedByPlayer() != null) {
			gameSessionFinishedByPlayer = data.getContent().getGameSessionsFinishedByPlayer();
		}else {
			data.getContent().setGameSessionsFinishedByPlayer(gameSessionFinishedByPlayer);
		}
		
		if(data.getContent().getGameSessionsFinishedByManager() != null) {
			gameSessionFinishedByManager = data.getContent().getGameSessionsFinishedByManager();
		}else {
			data.getContent().setGameSessionsFinishedByManager(gameSessionFinishedByManager);
		}
		
		if(data.getContent().getGameSessionsFinishedByTimeout() != null) {
			gameSessionFinishedByTimeout = data.getContent().getGameSessionsFinishedByTimeout();
		}else {
			data.getContent().setGameSessionsFinishedByTimeout(gameSessionFinishedByTimeout);
		}
		
		
		if(data.getContent().getLb1() != null) {
			game_1_data = data.getContent().getLb1();
		}else {
			data.getContent().setLb1(game_1_data);
		}
		if(data.getContent().getLb2() != null) {
			game_2_data = data.getContent().getLb2();
		}else {
			data.getContent().setLb2(game_2_data);
		}	
		
		if(data.getContent().getLbC() != null) {
			game_3_data = data.getContent().getLbC();
		}else {
			data.getContent().setLbC(game_3_data);
		}
		
		if(data.getContent().getLbD() != null) {
			game_4_data = data.getContent().getLbD();
		}else {
			data.getContent().setLbD(game_4_data);
		}
		
		
		return data;
	}
	
	*/
		
	@RequestMapping(value="/getpaneldata", method = RequestMethod.GET)
    public AdminPanelMessage getPanelData() {
		AdminPanelMessage data = new AdminPanelMessage();
		
		data.setMessageFor("äll");
		data.setType(AdminPanelMessageType.DATA);
		
		AdminPanelMessageData messageData = new AdminPanelMessageData();
		
		//randomNumber = random.nextInt(upperBound - lowerBound) + lowerBound;
		messageData.setGameSessionsLaunched(gameSessionLaunched);		
		messageData.setGameSessionsFinishedByPlayer(gameSessionFinishedByPlayer);		
		messageData.setGameSessionsFinishedByManager(gameSessionFinishedByManager);		
		messageData.setGameSessionsFinishedByTimeout(gameSessionFinishedByTimeout);	
		messageData.setLb1(game_1_data);
		messageData.setLb2(game_2_data);
		
		messageData.setLbC(game_3_data);
		messageData.setLbD(game_4_data);
		
		data.setContent(messageData);
		return data;
		
    }
	
	
	
	@RequestMapping(value="/getcontestdashoard", method = RequestMethod.GET)
    public ObjectNode getContestDashboardData(@RequestParam(value = "companyid",required=true) String companyId, @RequestParam(value = "contestid", required=true)  String contestId) {
		
		Httputil httputil = new Httputil();
		
		return httputil.getContestDasboardData(companyId, contestId);
		
    }
	
}