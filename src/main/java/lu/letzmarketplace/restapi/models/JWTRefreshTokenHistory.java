package lu.letzmarketplace.restapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "jwt_refresh_token_history")
public class JWTRefreshTokenHistory {

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId;
}
