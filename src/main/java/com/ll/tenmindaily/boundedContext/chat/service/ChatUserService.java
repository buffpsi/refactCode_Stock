package com.ll.tenmindaily.boundedContext.chat.service;


import com.ll.tenmindaily.boundedContext.chat.entity.ChatUser;
import com.ll.tenmindaily.boundedContext.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatRoomUserRepository;


    public ChatUser findById(Long chatRoomUserId) {
        return chatRoomUserRepository.findById(chatRoomUserId).orElseThrow();
    }
}
