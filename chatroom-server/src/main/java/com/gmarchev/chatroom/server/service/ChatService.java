package com.gmarchev.chatroom.server.service;

import java.time.LocalDateTime;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.dto.JoinChatRequest;
import com.gmarchev.chatroom.server.dto.MessageChatRequest;
import com.gmarchev.chatroom.server.model.ChatMessage;
import com.gmarchev.chatroom.server.model.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final SimpMessagingTemplate messagingTemplate;

	public void handleUserJoin(JoinChatRequest request) {

		ChatMessage joinMessage = ChatMessage.builder()
				.type(MessageType.JOIN)
				.sender(request.getSender())
				.timestamp(LocalDateTime.now())
				.build();

		broadcast(joinMessage);
	}

	private void broadcast(ChatMessage message) {

		messagingTemplate.convertAndSend(Constants.PUBLIC_CHANNEL, message);
	}

	public void handleMessage(MessageChatRequest request) {

		ChatMessage message = ChatMessage.builder()
				.type(MessageType.CHAT)
				.sender(request.getSender())
				.content(request.getContent())
				.timestamp(LocalDateTime.now())
				.build();

		broadcast(message);
	}
}
