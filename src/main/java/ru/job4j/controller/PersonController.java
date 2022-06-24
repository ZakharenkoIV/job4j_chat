package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/persons")
public class PersonController implements BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PersonController.class.getSimpleName());

    private final PersonService personService;
    private final ObjectMapper objectMapper;

    public PersonController(PersonService personService, ObjectMapper objectMapper) {
        this.personService = personService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/all")
    public List<Person> getAll() {
        return personService.findAll();
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        if (isNullState(person)) {
            throw new NullPointerException(
                    "Поле ".concat(getNamesNullFields(person))
                            .concat(" не может быть пустым"));
        }
        personService.save(person);
    }

    @PatchMapping("/{personId}")
    public void patchPerson(@RequestBody HashMap<String, String> body,
                            @PathVariable long personId) throws Exception {
        var person = personService.getById(personId);
        transferData(body, person);
        personService.save(person);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void notUniqueEmailExceptionHandler(Exception e, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", "Пользователь с таким email уже существует");
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

    private boolean isNullState(Person person) {
        return person.getName() == null
                || person.getEmail() == null
                || person.getPassword() == null;
    }

    private String getNamesNullFields(Person person) {
        StringJoiner massage = new StringJoiner(", ");
        if (person.getName() == null) {
            massage.add("имя");
        }
        if (person.getEmail() == null) {
            massage.add("email");
        }
        if (person.getPassword() == null) {
            massage.add("пароль");
        }
        return massage.toString();
    }
}