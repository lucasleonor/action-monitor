package com.betvictor.test.messaging.actionmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "datetime", nullable = false)
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "send_to")
    private User to;
    @ManyToOne
    @JoinColumn(name = "sent_from")
    private User from;
}
