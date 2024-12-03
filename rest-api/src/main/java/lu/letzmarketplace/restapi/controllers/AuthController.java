package lu.letzmarketplace.restapi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lu.letzmarketplace.restapi.dto.*;
import lu.letzmarketplace.restapi.mappers.UserLoginMapper;
import lu.letzmarketplace.restapi.mappers.UserRegistrationMapper;
import lu.letzmarketplace.restapi.mappers.UserMapper;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Implements tests
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserLoginMapper userLoginMapper;

    @PostMapping("register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserRegistrationRequestDTO dto) {
        User result = authService.register(userRegistrationMapper.toEntity(dto));
        if (result == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(userMapper.toDto(result), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<JWTResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto) {
        JWTResponseDTO result = authService.login(userLoginMapper.toEntity(dto));
        if (result == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<JWTResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO dto) {
        JWTResponseDTO result = authService.refresh(dto.getRefresh());
        if (result == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
