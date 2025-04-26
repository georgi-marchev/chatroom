package com.gmarchev.chatroom.server.service;

import java.time.LocalDateTime;
import java.util.List;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.dto.JoinChatRequest;
import com.gmarchev.chatroom.server.dto.MessageChatRequest;
import com.gmarchev.chatroom.server.model.ChatMessage;
import com.gmarchev.chatroom.server.model.MessageType;
import com.gmarchev.chatroom.server.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

	public static final String SYSTEM = "System";

	private final SimpMessagingTemplate messagingTemplate;

	private final ChatMessageRepository chatMessageRepository;

	public void handleUserJoin(JoinChatRequest request, String sessionId) {

		broadcastHistoryToUser(sessionId);

		ChatMessage joinMessage = persistJoinMessage(request);

		messagingTemplate.convertAndSend(Constants.PUBLIC_CHANNEL, joinMessage);
	}

	private void broadcastHistoryToUser(String sessionId) {

		List<ChatMessage> messages = chatMessageRepository.findTop100ByOrderByTimestampDesc();

		messagingTemplate.convertAndSendToUser(
				sessionId,
				Constants.HISTORY_CHANNEL,
				messages,
				createHistoryBroadcastHeaders(sessionId));
	}

	private MessageHeaders createHistoryBroadcastHeaders(String sessionId) {

		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

	private ChatMessage persistJoinMessage(JoinChatRequest request) {

		String username = request.getSender();

		ChatMessage joinMessage = ChatMessage.builder()
				.type(MessageType.JOIN)
				.sender(SYSTEM)
				.content(username + " has joined")
				.timestamp(LocalDateTime.now())
				.build();

		chatMessageRepository.save(joinMessage);

		return joinMessage;
	}

	public void handleMessage(MessageChatRequest request) {

		ChatMessage message = ChatMessage.builder()
				.type(MessageType.CHAT)
				.sender(request.getSender())
				.content(request.getContent())
				.timestamp(LocalDateTime.now())
				.build();

		chatMessageRepository.save(message);

		messagingTemplate.convertAndSend(Constants.PUBLIC_CHANNEL, message);
	}

	public void handleUserLeave(String username) {

		ChatMessage message = ChatMessage.builder()
				.sender(SYSTEM)
				.type(MessageType.LEAVE)
				.content(username + " has left")
				.timestamp(LocalDateTime.now())
				.build();

		chatMessageRepository.save(message);

		messagingTemplate.convertAndSend(Constants.PUBLIC_CHANNEL, message);
	}
}
