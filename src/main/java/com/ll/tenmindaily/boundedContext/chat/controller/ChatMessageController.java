package com.ll.tenmindaily.boundedContext.chat.controller;




import com.ll.tenmindaily.base.rq.Rq;
import com.ll.tenmindaily.boundedContext.chat.dto.ChatMessageDto;

import com.ll.tenmindaily.boundedContext.chat.response.SignalResponse;
import com.ll.tenmindaily.boundedContext.chat.service.ChatMessageService;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.Principal;
import java.util.List;

import static com.ll.tenmindaily.boundedContext.chat.entity.ChatMessageType.MESSAGE;
import static com.ll.tenmindaily.boundedContext.chat.response.SignalType.NEW_MESSAGE;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;
    @Autowired
    private  MemberService memberService;


    @MessageMapping("/chats/{roomId}/sendMessage")
    @SendTo("/topic/chats/{roomId}")
    public SignalResponse sendChatMessage(@DestinationVariable Long roomId, String request, Principal principal)  {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(requestAttributes, true);
        chatMessageService.createAndSave(request, memberService.findByUserIdWithoutOptional(principal.getName()), roomId, MESSAGE);

        return SignalResponse.builder()
                .type(NEW_MESSAGE)
                .build();
    }

    /*@MessageExceptionHandler
    public void handleException(Exception ex) {
        System.out.println("예외 발생!!");
    }*/

    @GetMapping("/rooms/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> findAll(
            @PathVariable Long roomId, @RequestParam(defaultValue = "0") Long fromId){

        List<ChatMessageDto> chatMessageDtos =
                chatMessageService.getByChatRoomIdAndUserIdAndFromId(roomId, fromId);

        return chatMessageDtos;
    }
}
