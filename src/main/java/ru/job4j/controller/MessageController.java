package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return new ResponseEntity<>(
                this.messageService.saveMessage(message),
                HttpStatus.OK
        );
    }
}
