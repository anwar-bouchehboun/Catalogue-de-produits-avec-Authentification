package com.authentification.produit.auth.entity;

import javax.persistence.*;

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

    private String designation;

    private Double prix;

    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;


}
