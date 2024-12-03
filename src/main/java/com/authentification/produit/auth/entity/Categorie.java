package com.authentification.produit.auth.entity;

import lombok.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Le nom ne peut pas être nul")
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 5, max = 50, message = "Le nom doit contenir entre 5 et 50 caractères")
    private String nom;

    @Column(length = 255)
    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produit> produits = new ArrayList<>();
}
