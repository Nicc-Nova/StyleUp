package com.StyleUp.backend.repositories;
import com.StyleUp.backend.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEmail(String email);
    List<Invitation> findByOwnerId(Long userId);
}