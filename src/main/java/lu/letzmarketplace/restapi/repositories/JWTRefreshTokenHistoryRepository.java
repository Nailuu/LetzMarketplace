package lu.letzmarketplace.restapi.repositories;

import lu.letzmarketplace.restapi.models.JWTRefreshTokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JWTRefreshTokenHistoryRepository extends JpaRepository<JWTRefreshTokenHistory, String> {
    Optional<JWTRefreshTokenHistory> findByUserId(UUID userId);
    void updateByUserId(UUID userId, JWTRefreshTokenHistory jwtRefreshTokenHistory);
}
