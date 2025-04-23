package com.gmarchev.chatroom.server.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

	private MessageType type;
	private String sender;
	private String content;
	private LocalDateTime timestamp;
}
