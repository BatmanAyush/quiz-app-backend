package com.revision.tool.controller;

import com.revision.tool.dao.ClientRepo;
import com.revision.tool.model.Client;
import com.revision.tool.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final ClientRepo clientRepo;
    private final JwtService jwtService;

    public AuthController(ClientRepo clientRepo, JwtService jwtService) {
        this.clientRepo = clientRepo;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint hit after successful Google OAuth login.
     * IMPORTANT: This must be handled by your STATEFUL security chain.
     */
    @GetMapping("/api/auth/oauth-success")
    public void oauthSuccess(
            @AuthenticationPrincipal OAuth2User user,
            HttpServletResponse response
    ) throws IOException {

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No OAuth2 principal in session"
            );
        }

        // Extract Google userinfo attributes
        String googleId = user.getAttribute("sub");     // stable Google user id
        String email    = user.getAttribute("email");
        String name     = user.getAttribute("name");

        if (googleId == null && email == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "OAuth2 user missing identifiers"
            );
        }

        // Upsert Client (prefer googleId; fall back to email if needed)
        Client client = clientRepo.findByGoogleId(googleId != null ? googleId : "")
                .or(() -> {
                    if (email == null) return Optional.empty();
                    // if you add repo.findByEmail, use it here; otherwise create new
                    return Optional.empty();
                })
                .orElseGet(() -> {
                    Client c = new Client();
                    c.setGoogleId(googleId);
                    c.setEmail(email);
                    c.setName(name != null ? name : (email != null ? email : "GoogleUser"));
                    // password remains null for OAuth users
                    return clientRepo.save(c);
                });

        // Issue your app JWT
        String jwt = jwtService.generateToken(client);

        // Redirect to frontend with token
        String frontend = System.getenv()
                .getOrDefault("FRONTEND_ORIGIN", "http://localhost:5173");
        response.sendRedirect(frontend + "/oauth-success?token=" + jwt);

        // Alternative JSON response (useful for SPAs):
        /*
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "user", Map.of(
                        "id", client.getId(),
                        "name", client.getName(),
                        "email", client.getEmail()
                )
        ));
        */
    }
}
