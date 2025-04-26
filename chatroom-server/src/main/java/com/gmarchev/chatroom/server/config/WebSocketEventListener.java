package com.gmarchev.chatroom.server.config;

import com.gmarchev.chatroom.server.Constants;
import com.gmarchev.chatroom.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final ChatService chatService;

	@EventListener
	public void handleWebSocketDisconnect(SessionDisconnectEvent event) {

		// When the user disconnects, create a message to be broadcasted to the chatroom
		String username = (String) StompHeaderAccessor
				.wrap(event.getMessage())
				.getSessionAttributes()
				.get(Constants.USERNAME_ATTR);

		if (username != null) {

			chatService.handleUserLeave(username);
		}
	}
}
