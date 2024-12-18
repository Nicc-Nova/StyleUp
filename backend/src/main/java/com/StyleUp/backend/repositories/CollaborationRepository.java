package com.StyleUp.backend.repositories;

import com.StyleUp.backend.models.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {
    List<Collaboration> findByUserId(Long userId);
    List<Collaboration> findByRoomId(Long roomId);
    Collaboration deleteByRoomIdAndUserId(Long roomId, Long userId);
    List<Collaboration> deleteByRoomId(Long roomId);
}
