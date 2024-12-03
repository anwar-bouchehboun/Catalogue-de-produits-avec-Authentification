package com.authentification.produit.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.authentification.produit.auth.entity.User;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
