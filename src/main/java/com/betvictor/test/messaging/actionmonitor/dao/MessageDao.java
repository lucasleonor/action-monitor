package com.betvictor.test.messaging.actionmonitor.dao;

import com.betvictor.test.messaging.actionmonitor.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends CrudRepository<Message, Long> {
}
