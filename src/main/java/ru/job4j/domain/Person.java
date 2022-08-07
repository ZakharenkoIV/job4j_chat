package ru.job4j.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must be not empty")
    private String name;

    @NotBlank(message = "Email must be not empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password must be not empty")
    private String password;

    @ManyToMany
    @JoinTable(name = "role_to_person",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @Transient
    private Set<Message> unreadMessages = new HashSet<>();

    public Person() {
    }

    public static Person of(long personId) {
        Person person = new Person();
        person.setId(personId);
        return person;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(Set<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public void addUnreadMessage(Message message) {
        this.unreadMessages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name)
                && Objects.equals(email, person.email)
                && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }
}
