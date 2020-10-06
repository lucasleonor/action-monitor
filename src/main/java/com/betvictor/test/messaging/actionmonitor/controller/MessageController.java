package com.betvictor.test.messaging.actionmonitor.controller;

import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import com.betvictor.test.messaging.actionmonitor.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    public static final String CHAT = "/private/chat";
    public static final String SPECIFIC_USER = "/private/user/queue/specific-user/";

    private final MessageService service;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(final MessageService service, final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.service = service;
    }

    @MessageMapping(CHAT)
    public void sendSpecific(@Payload MessageDTO msg, Principal principal) {
        msg.setFrom(principal.getName());
        service.save(msg);
        simpMessagingTemplate.convertAndSend(SPECIFIC_USER + msg.getTo(), msg);
    }
}
