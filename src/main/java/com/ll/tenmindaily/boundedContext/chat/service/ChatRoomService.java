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

    public ChatRoom createAndSave(String name, Long ownerId) {

        Member owner = memberService.findByIdElseThrow(ownerId);

        ChatRoom chatRoom = ChatRoom.create(name, owner);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        chatRoom.addChatUser(owner);

        return savedChatRoom;
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    public ChatRoomDto getByIdAndUserId(Long roomId, Long userId) {
        ChatRoom chatRoom = findById(roomId);

        chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow();

        return ChatRoomDto.fromChatRoom(chatRoom);
    }
}
