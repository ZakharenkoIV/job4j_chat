package ru.job4j.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must be not empty")
    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "message_to_room",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "message_id")})
    private Set<Message> messages = new HashSet<>();

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id)
                && Objects.equals(name, room.name)
                && Objects.equals(messages, room.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, messages);
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }
}
