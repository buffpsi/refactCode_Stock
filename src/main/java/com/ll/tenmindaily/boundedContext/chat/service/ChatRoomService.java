package com.ll.tenmindaily.boundedContext.chat.service;


import com.ll.tenmindaily.boundedContext.chat.dto.ChatRoomDto;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatRoom;
import com.ll.tenmindaily.boundedContext.chat.repository.ChatRoomRepository;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    public ChatRoom createAndSave(String name) {

        ChatRoom chatRoom = ChatRoom.create(name);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom;
    }

    public List<ChatRoom> findAll() { //채팅방 전체 찾기
        return chatRoomRepository.findAll();
    }


    public ChatRoom findById(Long roomId) { //채팅방 이름으로 찾기
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    public ChatRoomDto getById(Long roomId) { //채팅방 가져오기
        ChatRoom chatRoom = findById(roomId);
        return ChatRoomDto.fromChatRoom(chatRoom);
    }
}
