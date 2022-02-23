package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.util.Set;

@RestController
@RequestMapping("/chat")
public class RoomController {

    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/room/")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return new ResponseEntity<>(
                this.roomService.createRoom(room),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long roomId) {
        this.roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<Void> saveMessage(@RequestBody Message message,
                                            @PathVariable long roomId) {
        this.roomService.saveMessage(message, roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}?id={personId}")
    public ResponseEntity<Set<Message>> findUnreadMessages(
            @PathVariable long roomId, @PathVariable long personId) {
        return new ResponseEntity<>(
                this.roomService.findUnreadMessages(roomId, personId),
                HttpStatus.OK
        );
    }
}
