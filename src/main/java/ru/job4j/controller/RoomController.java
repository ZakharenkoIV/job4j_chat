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

    @GetMapping("/rooms/")
    public ResponseEntity<Set<Room>> findActiveRooms() {
        return new ResponseEntity<>(
                this.roomService.getActiveRooms(),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/rooms/", produces = "application/json")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return new ResponseEntity<>(
                this.roomService.createRoom(room),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Set<Message>> findAllRoomMessages(
            @PathVariable long roomId) {
        return new ResponseEntity<>(
                this.roomService.getAllRoomMessages(roomId),
                HttpStatus.OK
        );
    }

    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<Void> saveMessage(@RequestBody Message message,
                                            @PathVariable long roomId) {
        this.roomService.saveMessage(message, roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long roomId) {
        this.roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
