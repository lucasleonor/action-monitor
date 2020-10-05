package com.betvictor.test.messaging.actionmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableScheduling
public class UserController {
    private final SimpUserRegistry userRegistry;
    public static final String REGISTER = "/private/register";
    public static final String USERS = "/private/users";
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserController(final SimpUserRegistry userRegistry, final SimpMessagingTemplate simpMessagingTemplate) {
        this.userRegistry = userRegistry;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping(REGISTER)
    @Scheduled(fixedDelay = 3000)
    public void sendAll() {
        List<String> usernames = userRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());
        simpMessagingTemplate.convertAndSend(USERS, usernames);
    }

}
