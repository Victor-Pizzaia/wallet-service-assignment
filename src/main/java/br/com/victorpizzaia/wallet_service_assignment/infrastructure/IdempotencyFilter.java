package br.com.victorpizzaia.wallet_service_assignment.infrastructure;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IdempotencyFilter extends OncePerRequestFilter {

    @Value("${cache.ttl.idempotency}")
    private long idempotencyTtlMinutes;

    private final StringRedisTemplate redisTemplate;

    public IdempotencyFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Set<String> IGNORED_ROUTES = Set.of(
        "wallets",
        "auth"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String method = request.getMethod();
        String uriPath = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);

        if (!IGNORED_ROUTES.contains(uriPath) && "POST".equalsIgnoreCase(method)) {
            String idempotencyKey = request.getHeader("Idempotency-Key");

            if (idempotencyKey == null || idempotencyKey.isBlank()) {
                log.error("Missing Idempotency-Key header");
                sendError(response, "Missing Idempotency-Key header");
                return;
            }
            String idempotencyValue = "idempotency:" + request.getRequestURI() + ":" + idempotencyKey;

            Boolean exists = redisTemplate.hasKey(idempotencyValue);
            if (Boolean.TRUE.equals(exists)) {
                log.info("Duplicate request detected with Idempotency-Key: {}", idempotencyKey);
                sendError(response, "Duplicate request detected");
                return;
            }

            redisTemplate.opsForValue().set(
                idempotencyValue,
                idempotencyKey,
                Duration.ofMinutes(idempotencyTtlMinutes)
            );
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"%s\"}".formatted(message));
        response.getWriter().flush();
    }
}
