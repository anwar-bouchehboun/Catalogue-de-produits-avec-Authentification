package com.authentification.produit.auth.repository;

import com.authentification.produit.auth.entity.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepo extends JpaRepository<Categorie, Long> {
    Page<Categorie> findAll(Pageable pageable);

    Page<Categorie> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
