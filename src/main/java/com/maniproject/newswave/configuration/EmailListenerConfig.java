package com.maniproject.newswave.configuration;

import com.maniproject.newswave.entity.JavaMailReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

@Component
@EnableScheduling
@Slf4j
public class EmailListenerConfig {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private Store store;
    private Folder inbox;

    @PostConstruct
    public void initializeEmailConnection() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        log.info("Connecting to email server");
        Session session = Session.getInstance(props, null);
        store = session.getStore();
        store.connect("imap.gmail.com", emailUsername, emailPassword);
        log.info("Connected to email server {}",store.isConnected());
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        log.info("Connected to inbox {}",inbox.isOpen());
    }

    @Scheduled(fixedRate = 30000)
    public void checkForNewEmails() {
        try {
            log.info("Checking for new emails{}",inbox.isOpen());
            Message[] unreadMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            log.info("Found {} new messages", unreadMessages.length);
            for (Message message : unreadMessages) {
                try {
                    log.info("New message received from: {}", message.getFrom()[0]);
                    String sender= message.getFrom()[0].toString().split("<")[1].split(">")[0];
                    log.info("Sender: {}", sender);
                    String subject = message.getSubject();
                    message.setFlag(Flags.Flag.SEEN, true);
                    applicationEventPublisher.publishEvent(new JavaMailReceivedEvent(this, sender, subject));
                } catch (MessagingException e) {
                    log.error("Could not read or process message", e);
                }
            }
        } catch (MessagingException e) {
            log.error("Error while checking for new emails, attempting reconnection", e);
            reconnectToEmailServer();
        }
    }

    private void reconnectToEmailServer() {
        try {
            if (store != null) {
                store.close();
            }
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            log.info("Reconnecting to email server");
            Session session = Session.getInstance(props, null);
            store = session.getStore();
            store.connect("imap.gmail.com", emailUsername, emailPassword);
            log.info("Reconnected to email server: {}", store.isConnected());
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            log.info("Reconnected to inbox: {}", inbox.isOpen());
        } catch (MessagingException e) {
            log.error("Error while reconnecting to email server", e);
        }
    }

}
