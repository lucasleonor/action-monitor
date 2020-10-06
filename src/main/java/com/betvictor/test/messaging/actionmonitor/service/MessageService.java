package com.betvictor.test.messaging.actionmonitor.service;

import com.betvictor.test.messaging.actionmonitor.dao.MessageDao;
import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import com.betvictor.test.messaging.actionmonitor.model.Message;
import com.betvictor.test.messaging.actionmonitor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
    private final UserService userService;
    private final MessageDao dao;

    public MessageService(final UserService userService, final MessageDao dao) {
        this.userService = userService;
        this.dao = dao;
    }

    public void save(final MessageDTO dto) {
        Message message = convertDtoToModel(dto);
        message = dao.save(message);
        log.debug("Message {} saved", message.getId());
    }

    private Message convertDtoToModel(final MessageDTO dto) {
        User from = userService.findUserByUsername(dto.getFrom());
        User to = userService.findUserByUsername(dto.getTo());
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(dto.getText());
        message.setDateTime(dto.getDateTime());
        return message;
    }
}
