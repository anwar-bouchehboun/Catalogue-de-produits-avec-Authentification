package com.authentification.produit.auth.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La désignation ne peut pas être nulle")
    @NotBlank(message = "La désignation ne peut pas être vide")
    @Size(min = 3, max = 100, message = "La désignation doit contenir entre 3 et 100 caractères")
    private String designation;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0",  message = "Le prix doit être supérieur à 0")
    private Double prix;

    @NotNull(message = "La quantité ne peut pas être nulle")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;


}
