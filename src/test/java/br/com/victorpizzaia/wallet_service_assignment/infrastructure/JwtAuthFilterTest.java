package br.com.victorpizzaia.wallet_service_assignment.infrastructure;

import br.com.victorpizzaia.wallet_service_assignment.auth.domain.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtProvider jwtProvider;
    private JwtAuthFilter jwtAuthFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProvider.class);
        jwtAuthFilter = new JwtAuthFilter(jwtProvider);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenValidJwtProvided() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        String userId = UUID.randomUUID().toString();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtProvider.validateToken(jwt)).thenReturn(true);
        when(jwtProvider.getUserIdFromJwt(jwt)).thenReturn(userId);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userId, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().isEmpty());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenAuthorizationHeaderIsMalformed() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenJwtIsInvalid() throws ServletException, IOException {
        String jwt = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtProvider.validateToken(jwt)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
