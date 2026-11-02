package com.credora.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Protects the CV endpoints. Reads "Authorization: Bearer <token>", looks the
 * token up in the TokenStore, and - if valid - stores the user id as a request
 * attribute so controllers can read it with @RequestAttribute("userId").
 *
 * Public endpoints (login/signup, job listing, templates) are skipped entirely.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final TokenStore tokenStore;

    public AuthFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean isPublicJobGet = "GET".equalsIgnoreCase(request.getMethod())
                && (path.equals("/api/jobs") || path.equals("/api/jobs/categories"));

        // CORS preflight requests never carry the Authorization header - let them
        // through so Spring's CORS handling can answer, instead of rejecting them here.
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.startsWith("/api/auth")
                || path.equals("/api/templates")
                || isPublicJobGet;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
        Long userId = token != null ? tokenStore.resolveUserId(token) : null;

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Missing or invalid token. Please log in again.\"}");
            return;
        }

        request.setAttribute("userId", userId);
        chain.doFilter(request, response);
    }
}
