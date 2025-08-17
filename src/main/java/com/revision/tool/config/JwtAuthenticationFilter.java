package com.revision.tool.config;



import com.revision.tool.model.Client;

import com.revision.tool.model.UserPrinciple;
import com.revision.tool.service.JwtService;
import com.revision.tool.dao.ClientRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final ClientRepo clientRepository;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, ClientRepo clientRepository) {
        this.jwtService = jwtService;
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Enter filter jwtFilter");
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Filter");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Token Found");

            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        System.out.println(token);
        int id = 0;
        String username;

        try {
            username = jwtService.extractUsername(token);
            System.out.println(username);
            id = jwtService.extractUserIdAsInt(token);
        } catch (Exception e) {
            System.out.println("Error");
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println(id);
        System.out.println(username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Client user = clientRepository.findById(id).orElse(null);
            System.out.println(user);
            if (user != null && jwtService.isTokenValid(token, user)) {
                System.out.println("Valid");
                UserPrinciple principal = new UserPrinciple(user);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, null); // ❌ no roles

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }
}
