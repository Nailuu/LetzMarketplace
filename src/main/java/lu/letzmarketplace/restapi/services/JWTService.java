package lu.letzmarketplace.restapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lu.letzmarketplace.restapi.models.JWTRefreshTokenHistory;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.repositories.JWTRefreshTokenHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final JWTRefreshTokenHistoryRepository refreshTokenHistoryRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 minutes
    private long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        String token = Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getKey())
                .compact();

        storeOrUpdateRefreshToken(user, token);

        return token;
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException ex) {
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractClaims(token);
        if (claims == null) {
            return null;
        }

        return claimResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        Date now = new Date();

        return exp.before(now);
    }

    public boolean validateToken(String token, User user) {
        if (user == null) return false;

        final String userId = extractSubject(token);
        return (user.getId().toString().equals(userId) && !isTokenExpired(token));
    }

    public void storeOrUpdateRefreshToken(User user, String token) {
        JWTRefreshTokenHistory history = JWTRefreshTokenHistory.builder()
                .token(token)
                .userId(user.getId())
                .build();

        if (refreshTokenHistoryRepository.findByUserId(user.getId()).isPresent()) {
            refreshTokenHistoryRepository.updateByUserId(user.getId(), history);
        } else {
            refreshTokenHistoryRepository.save(history);
        }
    }

    public Optional<JWTRefreshTokenHistory> getRefreshTokenHistoryByUserId(UUID userId) {
        return refreshTokenHistoryRepository.findByUserId(userId);
    }
}
