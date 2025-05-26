package br.com.victorpizzaia.wallet_service_assignment.auth.web;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.AuthService;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginRequest;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;


class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthService authService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_ShouldReturnOkWithLoginResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest("12345678910", "password");
        LoginResponse loginResponse = new LoginResponse("jwt", 3600L);

        when(authService.login(ArgumentMatchers.any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)));

        verify(authService, times(1)).login(ArgumentMatchers.any(LoginRequest.class));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "");

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
