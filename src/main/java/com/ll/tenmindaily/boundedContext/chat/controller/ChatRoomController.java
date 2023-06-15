package com.ll.tenmindaily.boundedContext.chat.controller;

import com.ll.tenmindaily.base.rq.Rq;
import com.ll.tenmindaily.boundedContext.chat.dto.ChatRoomDto;
import com.ll.tenmindaily.boundedContext.chat.entity.ChatRoom;
import com.ll.tenmindaily.boundedContext.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {


    private final ChatRoomService chatRoomService;

    //채팅방 목록 조회
    @GetMapping("/rooms")
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findAll();

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/rooms/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model) {
        ChatRoomDto chatRoomDto = chatRoomService.getById(roomId);

        model.addAttribute("chatRoom", chatRoomDto);

        return "usr/chat/room";
    }


    //채팅방 개설
    @GetMapping("/rooms/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }


    //채팅방 개설
    @PostMapping("/rooms")
    public String newRoom(String roomName) {
        chatRoomService.createAndSave(roomName);

        return "redirect:/rooms";
    }
}