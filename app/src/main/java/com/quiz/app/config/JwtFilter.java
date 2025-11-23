package com.quiz.app.config;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.app.dto.ErrorResponseDTO;
import com.quiz.app.exception.InvalidJwtException;
import com.quiz.app.service.JWTService;
import com.quiz.app.service.MyUserDetailService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext context;
    private final ObjectMapper objectMapper;

    public JwtFilter(JWTService jwtService, ApplicationContext context, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.context = context;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUserName(token);
            } catch (InvalidJwtException e) {
                handleInvalidToken(response, e);
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                handleInvalidToken(response, new InvalidJwtException("Invalid JWT token"));
                return;
            }

        }
        filterChain.doFilter(request, response);
    }
    
    private void handleInvalidToken(HttpServletResponse response, Exception e) throws IOException {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
    
}
