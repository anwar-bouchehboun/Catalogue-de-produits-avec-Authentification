package com.authentification.produit.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieRespo {

    private Long id;


    private String nom;


    private String description;

    private List<ProduitRespo> produits;
}
