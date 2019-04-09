/**
 * 
 */
package com.onehuddle.websocket.controller;

import com.google.gson.Gson;
//import com.onehuddle.commons.contest.pojo.*;
import com.onehuddle.commons.pojo.ContestLeaderboardMessage;
import com.onehuddle.websocket.model.ChatMessage;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


/**
 * @author ragha
 *
 */
@Controller
public class MessageController {

    @MessageMapping("/leaderboard.sendMessage/{companyId}")
    @SendTo("/channel/public/{companyId}")
    public ChatMessage sendMessage(@DestinationVariable String companyId, @Payload ChatMessage chatMessage) {
    		System.out.println("sendMessage Received Chat Message");
    		Gson gson = new Gson();
    		System.out.println(gson.toJson(chatMessage));    		
        return chatMessage;
    }
    
    
    @MessageMapping("/leaderboard.sendMessage/{companyId}/{ContestId}")
    @SendTo("/channel/public/{companyId}/{ContestId}")
    public ContestLeaderboardMessage sendMessage(@DestinationVariable String companyId, @DestinationVariable String contestId, @Payload ContestLeaderboardMessage data) {
    		System.out.println("sendMessage Received Chat Message");
    		Gson gson = new Gson();
    		System.out.println(gson.toJson(data));    		
        return data;
    }

    @MessageMapping("/leaderboard.addUser")
    @SendTo("/channel/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
