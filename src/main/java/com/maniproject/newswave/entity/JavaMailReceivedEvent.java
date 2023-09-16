package com.maniproject.newswave.entity;

import org.springframework.context.ApplicationEvent;

public class JavaMailReceivedEvent extends ApplicationEvent {

    private final String sender;
    private final String subject;

    public JavaMailReceivedEvent(Object source, String sender, String subject) {
        super(source);
        this.sender = sender;
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }
}
