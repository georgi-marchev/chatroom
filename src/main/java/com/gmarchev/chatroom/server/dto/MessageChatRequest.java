package com.gmarchev.chatroom.server.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageChatRequest {

	private String sender;
	private String content;
}
