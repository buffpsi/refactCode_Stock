package com.ll.tenmindaily.boundedContext.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatMessage;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatMessageType;
import com.ll.tenmindaily.boundedContext.member.memberdto.MemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    @JsonProperty("message_id")
    private Long id;

    private long RoomId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sender")
    private MemberDto sender;


    public static ChatMessageDto fromChatMessage(ChatMessage chatMessage) {

        MemberDto memberDto = MemberDto.fromUser(chatMessage.getSender().getUser());

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .id(chatMessage.getId())
                .sender(memberDto)
                .content(chatMessage.getContent())
                .build();

        return chatMessageDto;
    }

    public static List<ChatMessageDto> fromChatMessages(List<ChatMessage> messages) {
        return messages.stream()
                .map(ChatMessageDto::fromChatMessage)
                .toList();
    }
}
