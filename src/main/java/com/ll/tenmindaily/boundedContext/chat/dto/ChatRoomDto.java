package com.ll.tenmindaily.boundedContext.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatRoom;
import com.ll.tenmindaily.boundedContext.member.memberdto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    @JsonProperty("chat_room_id")
    private Long id;

    @JsonProperty("name")
    private String name;

    public static ChatRoomDto fromChatRoom(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .build();

        return chatRoomDto;
    }

    public static ChatRoomDto create(String name){
        ChatRoomDto room = new ChatRoomDto();

        room.id = Long.valueOf(UUID.randomUUID().toString());
        room.name = name;
        return room;
    }
}
