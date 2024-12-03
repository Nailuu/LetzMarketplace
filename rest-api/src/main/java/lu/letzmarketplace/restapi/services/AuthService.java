package lu.letzmarketplace.restapi.services;

import lu.letzmarketplace.restapi.dto.JWTResponseDTO;
import lu.letzmarketplace.restapi.exceptions.EmailAlreadyExistsException;
import lu.letzmarketplace.restapi.exceptions.UsernameAlreadyExistsException;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager,
            JWTService jwtService
    ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        } else if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
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

    // TODO: Store refresh token, at each /refresh call, invalidate previous refresh token
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
        return JWTResponseDTO.builder()
                .access(jwtService.generateAccessToken(user))
                .refresh(jwtService.generateRefreshToken(user))
                .build();
    }
}
