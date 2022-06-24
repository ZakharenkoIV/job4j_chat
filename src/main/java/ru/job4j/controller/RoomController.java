package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/chat")
public class RoomController implements BaseController {

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
        if (room.getName() == null) {
            throw new NullPointerException("Имя комнаты не может быть пустым");
        }
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
        if (message.getContent() == null) {
            throw new NullPointerException("Сообщение не может быть пустым");
        }
        this.roomService.saveMessage(message, roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long roomId) {
        this.roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{roomId}")
    public void patchRoom(@RequestBody HashMap<String, String> body,
                          @PathVariable long roomId) throws Exception {
        var room = roomService.getById(roomId);
        transferData(body, room);
        roomService.save(room);
    }
}
