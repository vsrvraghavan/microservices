/**
 * 
 */
package com.onehuddle.websocket.controller;

import com.google.gson.Gson;
import com.onehuddle.websocket.model.ChatMessage;
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
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/channel/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    		System.out.println("sendMessage Received Chat Message");
    		Gson gson = new Gson();
    		System.out.println(gson.toJson(chatMessage));    		
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/channel/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
