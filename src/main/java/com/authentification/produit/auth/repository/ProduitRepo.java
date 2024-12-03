package com.authentification.produit.auth.repository;

import com.authentification.produit.auth.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepo extends JpaRepository<Produit, Long> {
    @Query("SELECT p FROM Produit p WHERE p.categorie.nom = :nomCategorie")
    Page<Produit> findByCategorie(String nomCategorie, Pageable pageable);
    @Query("SELECT p FROM Produit p WHERE p.categorie.nom IN :categories")
    Page<Produit> findByCategorie_Nom(List<String> categories, Pageable pageable);


    Page<Produit> findByDesignationContainingIgnoreCase(String designation, Pageable pageable);

    Page<Produit> findByCategorieNomContainingIgnoreCase(String nomCategorie, Pageable pageable);
}
