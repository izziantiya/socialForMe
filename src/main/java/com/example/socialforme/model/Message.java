package com.example.socialforme.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_id")
    private Long sender;

    @Column(name = "recipient_id")
    private Long recipient;

    @Column(name = "time")
    private String time;

    @Column(name = "content")
    private String content;

    private Long dialog;
}
