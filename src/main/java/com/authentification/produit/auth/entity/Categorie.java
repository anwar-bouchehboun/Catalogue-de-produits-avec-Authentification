package com.authentification.produit.auth.entity;

import lombok.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    private String nom;

    @Size(max = 255)
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Produit> produits;
}
