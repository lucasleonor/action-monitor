package com.betvictor.test.messaging.actionmonitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class MessageDTO implements Serializable {
    private String text;
    private String from;
    private String to;
    private ZonedDateTime dateTime;
}
