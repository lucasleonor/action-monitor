package com.betvictor.test.messaging.actionmonitor.service;

import com.betvictor.test.messaging.actionmonitor.dao.MessageDao;
import com.betvictor.test.messaging.actionmonitor.dao.UserDao;
import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import com.betvictor.test.messaging.actionmonitor.model.Message;
import com.betvictor.test.messaging.actionmonitor.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MessageService {
    private final UserDao userDao;
    private final MessageDao messageDao;

    public MessageService(final UserDao userDao, final MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    public void save(final MessageDTO dto) {
        Message message = convertDtoToModel(dto);
        messageDao.save(message);
    }

    private Message convertDtoToModel(final MessageDTO dto) {
        Optional<User> from = userDao.findUserByUsername(dto.getFrom());
        Optional<User> to = userDao.findUserByUsername(dto.getTo());
        Message message = new Message();
        message.setFrom(from.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        message.setTo(to.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        message.setText(dto.getText());
        message.setDateTime(dto.getDateTime());
        return message;
    }
}
