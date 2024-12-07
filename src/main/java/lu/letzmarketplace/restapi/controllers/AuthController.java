package lu.letzmarketplace.restapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lu.letzmarketplace.restapi.dto.*;
import lu.letzmarketplace.restapi.exceptions.InvalidJWTTokenException;
import lu.letzmarketplace.restapi.mappers.UserMapper;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserRegistrationRequestDTO dto) {
        User result = authService.register(
                User.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .username(dto.getUsername())
                        .build()
        );

        return new ResponseEntity<>(userMapper.toDto(result), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<JWTResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto) {
        JWTResponseDTO result = authService.login(
                User.builder()
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .build()
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<JWTResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO dto) {
        JWTResponseDTO result = authService.refresh(dto.getRefresh());
        if (result == null)
            throw new InvalidJWTTokenException();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("verify")
    public ResponseEntity<UserDTO> verify(@RequestParam @NotNull String token) {
        User user = authService.verifyEmail(token);

        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }
}
