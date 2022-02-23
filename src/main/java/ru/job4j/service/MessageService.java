package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messages;

    public MessageService(MessageRepository messages) {
        this.messages = messages;
    }

    public Message saveMessage(Message message) {
        return messages.save(message);
    }

}
