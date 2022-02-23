package ru.job4j.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public Set<Message> findUnreadMessages(final long roomId, final long personId) {
        Optional<Room> room = activeRooms.stream()
                .filter(r -> r.getId().equals(roomId)).findFirst();
        if (room.isPresent()) {
            Optional<Person> person = room.get().getPersons().stream()
                    .filter(p -> p.getId().equals(personId)).findFirst();
            if (person.isPresent()
                    && person.get().getUnreadMessages() != null
                    && person.get().getUnreadMessages().size() != 0) {
                Set<Message> unreadMessages = person.get().getUnreadMessages();
                person.get().setUnreadMessages(new HashSet<>());
                return unreadMessages;
            }
        }
        return new HashSet<>();
    }

    public void saveMessage(final Message newMessage, final long roomId) {
        Message savedMessage = rest.postForObject(
                MESSAGES_API + "/", newMessage, Message.class);

        activeRooms.stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .ifPresent(room -> {
                    room.addMessage(savedMessage);
                    room.getPersons().forEach(person -> person.addUnreadMessage(savedMessage));
                    rooms.save(room);
                });
    }
}
