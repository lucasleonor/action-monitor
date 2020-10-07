package com.betvictor.test.messaging.actionmonitor.service;

import com.betvictor.test.messaging.actionmonitor.dao.MessageDao;
import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import com.betvictor.test.messaging.actionmonitor.model.Message;
import com.betvictor.test.messaging.actionmonitor.model.User;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class MessageServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private MessageDao dao;
    private MessageService service;

    @BeforeEach
    void setUp() {
        service = new MessageService(userService, dao);
    }

    @Test
    void save() {
        String from = RandomString.make();
        String to = RandomString.make();
        User toUser = new User();
        toUser.setUsername(to);
        User fromUser = new User();
        fromUser.setUsername(from);
        String text = RandomString.make();
        ZonedDateTime dateTime = ZonedDateTime.now();
        MessageDTO messageDTO = new MessageDTO(text, from, to, dateTime);
        Message message = new Message(0, text, dateTime, toUser, fromUser);

        when(userService.findUserByUsername(from)).thenReturn(fromUser);
        when(userService.findUserByUsername(to)).thenReturn(toUser);
        when(dao.save(message)).thenReturn(message);
        service.save(messageDTO);
        verify(dao, times(1)).save(message);
        verify(userService, times(1)).findUserByUsername(to);
        verify(userService, times(1)).findUserByUsername(from);
    }
}