package com.example.reactchesslive.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JWTTokenFilter(UserDetailsService userDetailsService, JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.split(" ")[1].trim();
        Jws<Claims> jws = jwtUtils.parse(token);

        if (jws == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //         Get user identity and set it on the spring security context
        UserDetails userDetails = userDetailsService.loadUserByUsername(jws.getBody().getSubject());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
