package com.exam.utility.security;

import com.exam.utility.entity.User;
import com.exam.utility.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * Intercepts every authenticated request and blocks access to protected endpoints
 * when the user's forcePasswordChange flag is set to true.
 *
 * Only /auth/change-password is allowed until the user changes their password.
 * This enforces the first-login password change policy for admin-created accounts.
 */
@Component
@RequiredArgsConstructor
public class ForcePasswordChangeFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getServletPath();
        // Allow the change-password endpoint and auth endpoints to pass through
        if (path.equals("/auth/change-password") || path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = authentication.getName();
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.isForcePasswordChange()) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    objectMapper.writeValue(response.getWriter(), Map.of(
                        "success", false,
                        "message", "You must change your password before accessing this resource.",
                        "data", null
                    ));
                } catch (IOException ignored) {}
            }
        });

        // Only continue if response hasn't been committed (i.e., no forcePasswordChange block was written)
        if (!response.isCommitted()) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui") || path.startsWith("/api-docs") || path.startsWith("/actuator");
    }
}
