package com.gmarchev.chatroom.server.repository;

import java.util.List;

import com.gmarchev.chatroom.server.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findTop100ByOrderByTimestampDesc();
}
