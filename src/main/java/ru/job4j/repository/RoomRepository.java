package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.domain.Room;

import java.util.Set;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @NonNull
    @Query("from Room r")
    Set<Room> findAll();
}
