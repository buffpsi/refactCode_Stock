package com.ll.tenmindaily.boundedContext.chat.repository;


import com.ll.tenmindaily.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
