package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import java.beans.Statement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        if (message.getContent() == null) {
            throw new NullPointerException("Сообщение не может быть пустым");
        }
        return new ResponseEntity<>(
                this.messageService.save(message),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{messageId}")
    public void patchMessage(@RequestBody HashMap<String, String> body,
                             @PathVariable long messageId) throws Exception {
        var person = messageService.getById(messageId);
        for (String name : body.keySet()) {
            if (Arrays.stream(person.getClass().getDeclaredFields())
                    .map(Field::getName)
                    .noneMatch(p -> p.substring(p.lastIndexOf(".") + 1).equals(name))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ключ \"" + name + "\" не найден");
            }
            new Statement(person,
                    "set" + name.substring(0, 1).toUpperCase() + name.substring(1),
                    new String[]{body.get(name)}).execute();
        }
        messageService.save(person);
    }
}
