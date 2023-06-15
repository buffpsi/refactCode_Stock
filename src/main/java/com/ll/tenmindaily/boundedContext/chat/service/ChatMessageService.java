package com.ll.tenmindaily.boundedContext.chat.service;


import com.ll.tenmindaily.boundedContext.chat.dto.ChatMessageDto;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatMessage;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatMessageType;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatRoom;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatUser;
import com.ll.tenmindaily.boundedContext.chat.repository.ChatMessageRepository;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage createAndSave(String content, Member user, Long chatRoomId, ChatMessageType type) {

        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoom.addChatUser(user);



        ChatUser sender = chatRoom.getChatUsers().stream()
                .filter(chatUser -> chatUser.getUser().getUserId().equals(user.getUserId())).findFirst().orElseThrow();

        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .sender(sender)
                .type(type)
                .chatRoom(chatRoom)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getByChatRoomIdAndUserIdAndFromId(Long roomId, Long fromId) {


        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(roomId);

        List<ChatMessage> list = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getId() > fromId)
                .sorted(Comparator.comparing(ChatMessage::getId))
                .toList();

        return ChatMessageDto.fromChatMessages(list);
    }
}
