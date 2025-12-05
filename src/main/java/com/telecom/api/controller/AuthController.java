package com.telecom.api.controller;

import com.telecom.api.dto.AuthRequest;
import com.telecom.api.dto.AuthResponse;
import com.telecom.api.entity.User;
import com.telecom.api.enum_pack.Role;
import com.telecom.api.repository.UserRepository;
import com.telecom.api.security.JwtUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // Register toggle (DEV only)
    @Value("${app.auth.allow-register:true}")
    private boolean allowRegister;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // ============================
    // REGISTER (DEV ONLY)
    // ============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req,
                                      @RequestParam(defaultValue = "USER") String role) {

        // Block register in PROD environments
        if (!allowRegister) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Registration is disabled");
        }

        // If username exists
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Assign role: ADMIN or USER
        Role assignedRole = role.equalsIgnoreCase("ADMIN")
                ? Role.ROLE_ADMIN
                : Role.ROLE_USER;

        // Create user with BCrypt password
        User newUser = new User(
                req.getUsername(),
                encoder.encode(req.getPassword()),
                assignedRole
        );

        userRepo.save(newUser);
        return ResponseEntity.ok("registered");
    }

    // ============================
    // LOGIN → returns JWT token
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {

        User user = userRepo.findByUsername(req.getUsername()).orElse(null);

        // wrong username or password
        if (user == null || !encoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Create token with role claim
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
