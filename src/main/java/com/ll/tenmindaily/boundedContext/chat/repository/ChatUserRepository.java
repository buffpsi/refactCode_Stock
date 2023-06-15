package com.ll.tenmindaily.boundedContext.chat.repository;


import com.ll.tenmindaily.boundedContext.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
