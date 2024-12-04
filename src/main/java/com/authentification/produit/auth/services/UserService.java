package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.UserRequest;
import com.authentification.produit.auth.dto.response.UserResponse;
import com.authentification.produit.auth.entity.Role;
import com.authentification.produit.auth.entity.User;
import com.authentification.produit.auth.mapper.UserMapper;
import com.authentification.produit.auth.repository.RoleRepo;
import com.authentification.produit.auth.repository.UserRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import com.authentification.produit.auth.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public UserResponse register(UserRequest request) {
        if (userRepo.existsByLogin(request.getLogin())) {
            throw new ValidationException("Ce login est déjà utilisé");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        Role userRole = roleRepo.findByName(request.getRoles())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(request.getRoles());
                    return roleRepo.save(newRole);
                });

        user.setRoles(Arrays.asList(userRole));

        User savedUser = userRepo.save(user);
        return userMapper.toDto(savedUser);
    }

    public List<UserResponse> listUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateUserRoles(Long userId, List<String> roleNames) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundExceptionHndler("Utilisateur non trouvé"));

        List<Role> roles = roleNames.stream()
                .map(name -> {
                    Role role = new Role();
                    role.setName(name);
                    return role;
                })
                .collect(Collectors.toList());

        user.setRoles(roles);
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        
        User user = userRepo.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + username));

        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    log.debug("User {} has role: {}", username, role.getName());
                    return new SimpleGrantedAuthority(role.getName());
                })
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(), 
            user.getPassword(), 
            authorities
        );
    }

    public UserResponse authenticate(String login, String password) {
        log.info("Attempting authentication for user: {}", login);
        User user = userRepo.findByLogin(login)
                .orElseThrow(() -> new ValidationException("Login ou mot de passe incorrect"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Login ou mot de passe incorrect");
        }

        return userMapper.toDto(user);
    }
}
