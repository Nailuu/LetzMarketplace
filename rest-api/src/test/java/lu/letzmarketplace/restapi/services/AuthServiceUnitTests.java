package lu.letzmarketplace.restapi.services;

import lu.letzmarketplace.restapi.dto.JWTResponseDTO;
import lu.letzmarketplace.restapi.exceptions.EmailAlreadyExistsException;
import lu.letzmarketplace.restapi.exceptions.UsernameAlreadyExistsException;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("bob123")
                .email("bob@test.lu")
                .firstName("bob")
                .lastName("joe")
                .password("123")
                .build();
    }

    @Test
    @DisplayName("Test 1: Register user")
    @Order(1)
    void register() {
        // Pre-condition
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(bCryptPasswordEncoder.encode(user.getPassword())).willReturn(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        given(userRepository.save(user)).willReturn(user);

        // Action
        authService.register(user);

        // Assert & Verify
        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isNotEqualTo("123");
        assertThat(user.getPassword()).startsWith("$2a$12$");

        verify(userRepository).save(user);
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
        verify(bCryptPasswordEncoder).encode("123");
    }

    @Test
    @DisplayName("Test 2: Register user with same email")
    @Order(2)
    void registerWithSameEmail() {
        // Pre-condition
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));

        // Assert & Verify
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(user));

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Test 3: Register user with same username")
    @Order(3)
    void registerWithSameUsername() {
        // Pre-condition
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.ofNullable(user));

        // Assert & Verify
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(user));

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Test 4: Login user")
    @Order(4)
    void login() {
        // Pre-condition
        final String accessToken = "access123";
        final String refreshToken = "refresh123";

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(authenticationManager.authenticate(argThat(argument -> {
            if (argument instanceof UsernamePasswordAuthenticationToken token) {
                return token.getPrincipal().equals(user.getEmail()) &&
                        token.getCredentials().equals(user.getPassword());
            }
            return false;
        }))).willReturn(authentication);
        given(authentication.isAuthenticated()).willReturn(true);
        given(jwtService.generateAccessToken(user)).willReturn(accessToken);
        given(jwtService.generateRefreshToken(user)).willReturn(refreshToken);

        // Action
        JWTResponseDTO result = authService.login(user);

        // Assert & Verify
        assertThat(result).isNotNull();
        assertThat(result.getAccess()).isEqualTo(accessToken);
        assertThat(result.getRefresh()).isEqualTo(refreshToken);

        verify(userRepository).findByEmail(user.getEmail());
        verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    @Test
    @DisplayName("Test 5: Login user not found")
    @Order(5)
    void loginUserNotFound() {
        // Pre-condition
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

        // Assert & Verify
        assertThrows(BadCredentialsException.class, () -> authService.login(user));

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Test 6: Login user bad credentials")
    @Order(6)
    void loginUserBadCredentials() {
        // Pre-condition
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(authentication.isAuthenticated()).willReturn(false);

        // Assert & Verify
        assertThrows(BadCredentialsException.class, () -> authService.login(user));

        verify(userRepository).findByEmail(user.getEmail());
        verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).isAuthenticated();
    }
//
//    public JWTResponseDTO refresh(String refreshToken) {
//        String email = jwtService.extractEmail(refreshToken);
//        String type = jwtService.extractType(refreshToken);
//
//        if (email == null || type == null || !type.equals("refresh")) {
//            return null;
//        }
//
//        User user = userRepository.findByEmail(email)
//                .orElse(null);
//
//        if (!jwtService.validateToken(refreshToken, user)) {
//            return null;
//        }
//
//        assert user != null;
//        return JWTResponseDTO.builder()
//                .access(jwtService.generateAccessToken(user))
//                .refresh(jwtService.generateRefreshToken(user))
//                .build();
//    }

    @Test
    @DisplayName("Test 7: Refresh JWT token")
    @Order(7)
    void refresh() {
        // Pre-condition
        final String token = "token123";
        final String refreshToken = "refresh123";
        final String accessToken = "access123";

        given(jwtService.extractEmail(token)).willReturn(user.getEmail());
        given(jwtService.extractType(token)).willReturn("refresh");
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(jwtService.validateToken(token, user)).willReturn(true);
        given(jwtService.generateAccessToken(user)).willReturn(accessToken);
        given(jwtService.generateRefreshToken(user)).willReturn(refreshToken);

        // Action
        JWTResponseDTO result = authService.refresh(token);

        // Verify & Assert
        assertThat(result).isNotNull();
        assertThat(result.getAccess()).isEqualTo(accessToken);
        assertThat(result.getRefresh()).isEqualTo(refreshToken);

        verify(jwtService).extractEmail(token);
        verify(jwtService).extractType(token);
        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtService).validateToken(token, user);
        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    @Test
    @DisplayName("Test 8: Refresh JWT token with access token")
    @Order(8)
    void refreshAccessToken() {
        // Pre-condition
        final String token = "token123";

        given(jwtService.extractEmail(token)).willReturn(user.getEmail());
        given(jwtService.extractType(token)).willReturn("access");

        // Action
        JWTResponseDTO result = authService.refresh(token);

        // Assert & Verify
        assertThat(result).isNull();

        verify(jwtService).extractEmail(token);
        verify(jwtService).extractType(token);
    }

    @Test
    @DisplayName("Test 9: Refresh JWT token with invalid token")
    @Order(9)
    void refreshInvalidToken() {
        // Pre-condition
        final String token = "token123";

        given(jwtService.extractEmail(token)).willReturn(user.getEmail());
        given(jwtService.extractType(token)).willReturn("refresh");
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(jwtService.validateToken(token, user)).willReturn(false);

        // Action
        JWTResponseDTO result = authService.refresh(token);

        // Verify & Assert
        assertThat(result).isNull();

        verify(jwtService).extractEmail(token);
        verify(jwtService).extractType(token);
        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtService).validateToken(token, user);
    }
}