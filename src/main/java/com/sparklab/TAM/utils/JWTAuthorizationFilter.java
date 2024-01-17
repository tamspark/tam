package com.sparklab.TAM.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklab.TAM.helper.SpringContextHelper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().matches("/TAM/auth(.*)")
               || request.getServletPath().matches("/swagger-ui/index.html")
                || request.getServletPath().matches("/TAM/(.*)")
        ) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            JwtUtils tokenUtils = SpringContextHelper.getApplicationContext().getBean(JwtUtils.class);
            String username;
            String token;
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
                try {
                    username = tokenUtils.getSubject(token);

                    if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
                        if (tokenUtils.validateAccessToken(token)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    username, null,
                                    tokenUtils.getAuthorities(token));
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                    filterChain.doFilter(request, response);
                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String, String> map = new HashMap<>();
                    map.put("error_msg", e.getMessage());
                    new ObjectMapper().writeValue(response.getOutputStream(), map);
                }

            } else {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String, String> map = new HashMap<>();
                map.put("error", "UNAUTHORIZED");
                new ObjectMapper().writeValue(response.getOutputStream(), map);
            }
        }

    }
}
