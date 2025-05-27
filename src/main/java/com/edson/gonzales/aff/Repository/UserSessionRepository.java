package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.Entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByToken(String token);

    void deleteByToken(String token);
}
