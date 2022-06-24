package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.beans.Statement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public interface BaseController {

    default void transferData(HashMap<String, String> inputData, Object targetData)
            throws Exception {
        for (String name : inputData.keySet()) {
            if (Arrays.stream(targetData.getClass().getDeclaredFields())
                    .map(Field::getName)
                    .noneMatch(p -> p.substring(p.lastIndexOf(".") + 1).equals(name))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Ключ \"" + name + "\" не найден");
            }
            new Statement(targetData,
                    "set" + name.substring(0, 1).toUpperCase() + name.substring(1),
                    new String[]{inputData.get(name)}).execute();
        }
    }
}
