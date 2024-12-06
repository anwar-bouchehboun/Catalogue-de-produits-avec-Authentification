package com.authentification.produit.auth.controllers.AuthController;

import com.authentification.produit.auth.dto.request.UserRequest;
import com.authentification.produit.auth.dto.response.UserResponse;
import com.authentification.produit.auth.services.UserService;
import com.authentification.produit.auth.utils.ReponseMessage;
import com.authentification.produit.auth.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request, HttpServletRequest session) {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null && existingAuth.isAuthenticated() && 
            !(existingAuth instanceof AnonymousAuthenticationToken)) {
            throw new ValidationException("Vous êtes déjà connecté. Veuillez d'abord vous déconnecter.");
        }

       
        try {
            // Création du token d'authentification
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword());


            
            // Mise à jour du contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
            // Récupération des informations utilisateur
            UserResponse response = userService.authenticate(request.getLogin(), request.getPassword());
            
            // Création de la réponse
          /*  Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", response);
            responseBody.put("sessionId", session.getSession().getId());
            responseBody.put("message", "Authentification réussie");
            */
            log.info("Login successful for user: {}", request.getLogin());
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            log.error("Login failed for user: {}", request.getLogin());
            throw new ValidationException("Identifiants invalides");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null && existingAuth.isAuthenticated() && 
            !(existingAuth instanceof AnonymousAuthenticationToken)) {
            throw new ValidationException("Vous êtes déjà connecté. Veuillez d'abord vous déconnecter.");
        }
        log.info("Register  user !! Succes");
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {

        SecurityContextHolder.clearContext();
    return ResponseEntity.ok(ReponseMessage.success("Déconnexion réussie"));

    }

}
