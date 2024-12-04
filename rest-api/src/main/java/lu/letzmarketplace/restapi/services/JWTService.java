package lu.letzmarketplace.restapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lu.letzmarketplace.restapi.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

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

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getKey())
                .compact();
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
}
