package com.gmarchev.chatroom.server.config;

import java.time.LocalDateTime;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.model.ChatMessage;
import com.gmarchev.chatroom.server.model.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final SimpMessageSendingOperations messageSendingOperations;

	@EventListener
	public void handleWebSocketDisconnect(SessionDisconnectEvent event) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor
				.getSessionAttributes()
				.get(Constants.USERNAME_ATTR);

		if (username != null) {

			ChatMessage message = ChatMessage.builder()
					.sender(username)
					.type(MessageType.LEAVE)
					.timestamp(LocalDateTime.now())
					.build();

			// Send the leave event to the chat message queue
			messageSendingOperations.convertAndSend(Constants.PUBLIC_CHANNEL	, message);
		}
	}
}
