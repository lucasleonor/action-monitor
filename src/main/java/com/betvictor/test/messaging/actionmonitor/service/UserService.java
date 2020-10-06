package com.betvictor.test.messaging.actionmonitor.service;

import com.betvictor.test.messaging.actionmonitor.dao.UserDao;
import com.betvictor.test.messaging.actionmonitor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserDao dao;

    @Autowired
    public UserService(final UserDao dao) {
        this.dao = dao;
    }

    public User findUserByUsername(final String username) {
        Optional<User> user = dao.findUserByUsername(username);
        if(user.isEmpty()){
            log.info("User {} not found", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user.get();
    }
}
