package com.betvictor.test.messaging.actionmonitor.dao;

import com.betvictor.test.messaging.actionmonitor.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
