package com.authentification.produit.auth.dto.response;

import com.authentification.produit.auth.entity.Produit;


import java.util.List;

public class CategorieRespo {

    private Long id;


    private String nom;


    private String description;

    private List<Produit> produits;
}
