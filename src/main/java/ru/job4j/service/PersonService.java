package ru.job4j.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepository persons;
    private final BCryptPasswordEncoder encoder;

    public PersonService(PersonRepository persons,
                         BCryptPasswordEncoder encoder) {
        this.persons = persons;
        this.encoder = encoder;
    }

    public Person save(Person person) {
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Длинна пароля не может быть меньше 6 символов");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return persons.save(person);
    }

    public List<Person> findAll() {
        return StreamSupport
                .stream(persons.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public Person getById(Long id) {
        return persons.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Пользователя с таким ID не найдено"));
    }
}
