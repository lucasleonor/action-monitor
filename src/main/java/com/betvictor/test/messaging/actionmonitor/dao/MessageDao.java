package com.betvictor.test.messaging.actionmonitor.dao;

import com.betvictor.test.messaging.actionmonitor.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<Message, Integer> {
}
