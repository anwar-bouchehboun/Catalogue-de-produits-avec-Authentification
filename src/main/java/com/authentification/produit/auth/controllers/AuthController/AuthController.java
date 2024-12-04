package com.authentification.produit.auth.controllers.AuthController;

import com.authentification.produit.auth.dto.request.UserRequest;
import com.authentification.produit.auth.dto.response.UserResponse;
import com.authentification.produit.auth.services.UserService;
import com.authentification.produit.auth.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request, HttpSession session) {
        log.info("test ="+request);
        log.info("Login attempt for user: {}", request.getLogin());
        log.info("Session  = "+ session.getId());
        try {
            UserResponse response = userService.authenticate(request.getLogin(), request.getPassword());
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", response);
            responseBody.put("sessionId", session.getId());
            responseBody.put("message", "Authentification réussie");
            
            log.info("Login successful for user: {}", request.getLogin());
            return ResponseEntity.ok(responseBody);
        } catch (ValidationException e) {
            log.error("Login failed for user: {}", request.getLogin(), e);
            throw new ValidationException(e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        log.info("Register  user !! Succes");
        return ResponseEntity.ok(userService.register(request));
    }
      @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("fff");
    }
}