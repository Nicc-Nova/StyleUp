package com.StyleUp.backend.repositories;
import com.StyleUp.backend.models.Decoration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface DecorationRepository extends JpaRepository<Decoration, Long> {
    List<Decoration> findByFkr(Long fkUserId);
    List<Decoration> findByWishId(Long wishId);
}