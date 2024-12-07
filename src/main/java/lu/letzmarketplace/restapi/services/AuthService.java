package lu.letzmarketplace.restapi.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lu.letzmarketplace.restapi.dto.JWTResponseDTO;
import lu.letzmarketplace.restapi.exceptions.BadRequestException;
import lu.letzmarketplace.restapi.exceptions.EmailAlreadyExistsException;
import lu.letzmarketplace.restapi.exceptions.UsernameAlreadyExistsException;
import lu.letzmarketplace.restapi.models.JWTRefreshTokenHistory;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final MailerService mailerService;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        } else if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // TODO: Generate token
        user.setEmailVerifyToken("123");

        userRepository.save(user);

        // mailerService.sendUserVerificationEmail(user);

        return user;
    }

    public JWTResponseDTO login(User user) {
        User data = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials or user doesn't exist"));

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials or user doesn't exist");
        }

        return JWTResponseDTO.builder()
                .access(jwtService.generateAccessToken(data))
                .refresh(jwtService.generateRefreshToken(data))
                .build();
    }

    public JWTResponseDTO refresh(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);
        String type = jwtService.extractType(refreshToken);

        if (email == null || type == null || !type.equals("refresh")) {
            return null;
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (!jwtService.validateToken(refreshToken, user)) {
            return null;
        }
        assert user != null;

        JWTRefreshTokenHistory allowed = jwtService.getRefreshTokenHistoryByUserId(user.getId()).orElse(null);
        assert allowed != null;

        if (!refreshToken.equals(allowed.getToken())) {
            throw new BadRequestException("Refresh token is invalid due to newer refresh token");
        }

        return JWTResponseDTO.builder()
                .access(jwtService.generateAccessToken(user))
                .refresh(jwtService.generateRefreshToken(user))
                .build();
    }

    public User verifyEmail(String token) {
        User user = userRepository.findByEmailVerifyToken(token)
                .orElse(null);

        if (user == null) {
            throw new BadRequestException("Invalid token");
        }

        user.setEmailVerified(true);
        user.setEmailVerifyToken(null);

        userRepository.save(user);

        return user;
    }
}
