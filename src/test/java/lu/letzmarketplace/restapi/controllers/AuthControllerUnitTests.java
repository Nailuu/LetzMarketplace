package lu.letzmarketplace.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.letzmarketplace.restapi.dto.JWTResponseDTO;
import lu.letzmarketplace.restapi.dto.RefreshTokenRequestDTO;
import lu.letzmarketplace.restapi.dto.UserLoginRequestDTO;
import lu.letzmarketplace.restapi.dto.UserRegistrationRequestDTO;
import lu.letzmarketplace.restapi.mappers.UserMapper;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.services.AuthService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasKey;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerUnitTests {
    private final String endpoint = "/api/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserRegistrationRequestDTO userRegistrationRequestDTO;
    private UserLoginRequestDTO userLoginRequestDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("bob123")
                .email("bob@test.lu")
                .firstName("bob")
                .lastName("joe")
                .password("123456789")
                .build();

        userRegistrationRequestDTO = UserRegistrationRequestDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();

        userLoginRequestDTO = UserLoginRequestDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Test
    @DisplayName("Test 1: Register user")
    @Order(1)
    void register() throws Exception {
        // Pre-condition
        given(authService.register(user)).willReturn(user);
        given(userMapper.toDto(user)).willReturn(new UserMapper().toDto(user));

        // Action
        ResultActions response = mockMvc.perform(post(endpoint + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationRequestDTO)));

        // Expect
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", CoreMatchers.is(user.getEmail())))
                .andExpect(jsonPath("$.username", CoreMatchers.is(user.getUsername())))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(user.getLastName())))
                .andExpect(jsonPath("$", CoreMatchers.not(hasKey("password"))));
    }

    @Test
    @DisplayName("Test 2: Login user")
    @Order(2)
    void login() throws Exception {
        // Pre-condition
        final String accessToken = "access123";
        final String refreshToken = "refresh123";

        user = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        given(authService.login(user)).willReturn(
                JWTResponseDTO.builder()
                        .access(accessToken)
                        .refresh(refreshToken)
                        .build()
        );

        // Action
        ResultActions response = mockMvc.perform(post(endpoint + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginRequestDTO)));

        // Expect
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access", CoreMatchers.is(accessToken)))
                .andExpect(jsonPath("$.refresh", CoreMatchers.is(refreshToken)));
    }

    @Test
    @DisplayName("Test 3: Refresh JWT token")
    @Order(3)
    void refresh() throws Exception {
        // Pre-condition
        final String accessToken = "access123";
        final String refreshToken = "refresh123";

        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO(refreshToken);

        given(authService.refresh(refreshToken)).willReturn(
                JWTResponseDTO.builder()
                        .access(accessToken)
                        .refresh(refreshToken)
                        .build()
        );

        // Action
        ResultActions response = mockMvc.perform(post(endpoint + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Expect
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access", CoreMatchers.is(accessToken)))
                .andExpect(jsonPath("$.refresh", CoreMatchers.is(refreshToken)));
    }

    @Test
    @DisplayName("Test 4: Refresh JWT token invalid")
    @Order(4)
    void refreshInvalid() throws Exception {
        // Pre-condition
        final String refreshToken = "refresh123";

        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO(refreshToken);

        given(authService.refresh(refreshToken)).willReturn(null);

        // Action
        ResultActions response = mockMvc.perform(post(endpoint + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Expect
        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}