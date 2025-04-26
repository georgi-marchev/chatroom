package com.gmarchev.chatroom.server.controller;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.dto.JoinChatRequest;
import com.gmarchev.chatroom.server.dto.MessageChatRequest;
import com.gmarchev.chatroom.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat.addUser")
	public void joinChat(
			@Payload JoinChatRequest request,
			SimpMessageHeaderAccessor headers) {

		headers.getSessionAttributes().put(Constants.USERNAME_ATTR, request.getSender());
		chatService.handleUserJoin(request);
	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload MessageChatRequest request) {

		chatService.handleMessage(request);
	}
}
