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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final Rq rq;

    private final ChatRoomService chatRoomService;
    @GetMapping("/rooms")
    public String showRooms(Model model) {

        List<ChatRoom> chatRooms = chatRoomService.findAll();

        model.addAttribute("chatRooms", chatRooms);
        return "usr/chat/rooms";
    }

    @GetMapping("/rooms/{roomId}")
    public String showRoom(@PathVariable Long roomId, Model model) {

        ChatRoomDto chatRoomDto = chatRoomService.getByIdAndUserId(roomId, rq.getSecurityUser().getId());

        model.addAttribute("chatRoom", chatRoomDto);

        return "usr/chat/room";
    }

    @GetMapping("/rooms/new")
    public String showNewRoom() {
        return "usr/chat/newRoom";
    }



    @PostMapping("/rooms")
    public String newRoom(String roomName) {
        ChatRoom chatRoom = chatRoomService.createAndSave(roomName, rq.getSecurityUser().getId());

        return "redirect:/rooms/" + chatRoom.getId();
    }
}