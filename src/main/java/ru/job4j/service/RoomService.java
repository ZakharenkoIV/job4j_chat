package ru.job4j.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class RoomService {
    private static final String MESSAGES_API = "http://localhost:8080/messages";

    private final RestTemplate rest;

    private final RoomRepository rooms;

    private final Set<Room> activeRooms;

    public RoomService(RestTemplate rest, RoomRepository rooms) {
        this.rest = rest;
        this.rooms = rooms;
        activeRooms = rooms.findAll();
    }

    public Room createRoom(Room room) {
        Room newRoom = rooms.save(room);
        activeRooms.add(newRoom);
        return newRoom;
    }

    public void deleteRoom(final long roomId) {
        activeRooms.stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .ifPresent(activeRooms::remove);
        rooms.deleteById(roomId);
    }

    public Set<Message> getAllRoomMessages(final long roomId) {
        return activeRooms.stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .map(Room::getMessages)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account is not found. Please, check requisites."));
    }

    public void saveMessage(final Message newMessage, final long roomId) {
        Message savedMessage = rest.postForObject(
                MESSAGES_API + "/", newMessage, Message.class);

        activeRooms.stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .ifPresent(room -> room.addMessage(savedMessage));
    }

    public Set<Room> getActiveRooms() {
        return this.activeRooms.stream()
                .map(room -> {
                    Room roomDTO = new Room();
                    roomDTO.setId(room.getId());
                    roomDTO.setName(room.getName());
                    return roomDTO;
                }).collect(Collectors.toSet());
    }

    public Room getById(Long roomId) {
        return rooms.findById(roomId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Комната не найдена"));
    }

    public Room save(Room room) {
        return rooms.save(room);
    }
}
