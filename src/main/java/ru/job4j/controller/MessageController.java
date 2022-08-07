package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/messages")
public class MessageController implements BaseController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<Message> saveMessage(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                this.messageService.save(message),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{messageId}")
    public void patchMessage(@RequestBody HashMap<String, String> body,
                             @PathVariable long messageId) throws Exception {
        var massage = messageService.getById(messageId);
        transferData(body, massage);
        messageService.save(massage);
    }
}
