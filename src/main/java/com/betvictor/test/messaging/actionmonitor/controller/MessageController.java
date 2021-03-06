package com.betvictor.test.messaging.actionmonitor.controller;

import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import com.betvictor.test.messaging.actionmonitor.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.betvictor.test.messaging.actionmonitor.config.WebSocketConfig.SOCKETS;

@Slf4j
@Controller
public class MessageController {

    public static final String CHAT = SOCKETS + "/chat";
    public static final String SPECIFIC_USER = SOCKETS + "/user/queue/specific-user/";

    private final MessageService service;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(final MessageService service, final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.service = service;
    }

    @MessageMapping(CHAT)
    public void sendMessage(@Payload MessageDTO msg, Principal principal) {
        log.info("Message sent from '{}' to '{}' with text: {}", msg.getFrom(), msg.getTo(), msg.getText());
        msg.setFrom(principal.getName());
        service.save(msg);
        simpMessagingTemplate.convertAndSend(SPECIFIC_USER + msg.getTo(), msg);
    }
}
