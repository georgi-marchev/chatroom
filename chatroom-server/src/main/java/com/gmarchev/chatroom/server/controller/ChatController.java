package com.gmarchev.chatroom.server.controller;

import java.time.LocalDateTime;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@MessageMapping("/chat.sendMessage")
	@SendTo(Constants.PUBLIC_CHANNEL)
	public ChatMessage sendMessage(@Payload ChatMessage message) {

		message.setTimestamp(LocalDateTime.now());

		return message;
	}
	@MessageMapping("/chat.addUser")
	@SendTo(Constants.PUBLIC_CHANNEL)
	public ChatMessage joinChat(
			@Payload ChatMessage message,
			SimpMessageHeaderAccessor headerAccessor) {

		// Add username in Web Socket
		headerAccessor.getSessionAttributes().put(Constants.USERNAME_ATTR, message.getSender());

		return message;
	}
}
