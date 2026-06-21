package com.example.demo.future.RefreshToKenSession;

import com.example.demo.future.RefreshToKenSession.domain.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshToKenSessionRepository extends JpaRepository<RefreshTokenSession,Long> {
    Optional<RefreshTokenSession> findByJti(String jti);
    List<RefreshTokenSession> findByUserIdAndIsRevokedFalse(Long userId);
    Optional<RefreshTokenSession> findByJtiAndIsRevokedFalse(String jti);
}
