package com.ll.tenmindaily.boundedContext.chat.controller;


import com.ll.tenmindaily.base.rq.Rq;
import com.ll.tenmindaily.boundedContext.chat.dto.ChatMessageDto;
import com.ll.tenmindaily.boundedContext.chat.request.ChatMessageRequest;
import com.ll.tenmindaily.boundedContext.chat.response.SignalResponse;
import com.ll.tenmindaily.boundedContext.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.ll.tenmindaily.boundedContext.chat.entity.ChatMessageType.MESSAGE;
import static com.ll.tenmindaily.boundedContext.chat.response.SignalType.NEW_MESSAGE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final Rq rq;

    @MessageMapping("/chats/{roomId}/sendMessage")
    @SendTo("/topic/chats/{roomId}")
    public SignalResponse sendChatMessage(@DestinationVariable Long roomId, ChatMessageRequest request)  {

        log.info("content : {}", request.getContent());

        chatMessageService.createAndSave(request.getContent(), rq.getSecurityUser().getId(), roomId, MESSAGE);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    @MessageExceptionHandler
    public void handleException(Exception ex) {
        System.out.println("예외 발생!!");
    }

    @GetMapping("/rooms/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> findAll(
            @PathVariable Long roomId, @RequestParam(defaultValue = "0") Long fromId) {

        List<ChatMessageDto> chatMessageDtos =
                chatMessageService.getByChatRoomIdAndUserIdAndFromId(roomId, rq.getSecurityUser().getId(), fromId);

        return chatMessageDtos;
    }
}
