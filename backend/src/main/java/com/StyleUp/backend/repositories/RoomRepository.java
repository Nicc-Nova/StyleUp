package com.StyleUp.backend.repositories;
import com.StyleUp.backend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByFku(Long fkUserId);
}