package com.authentification.produit.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRespo {

    private Long id;
    private String designation;
    private Double prix;
    private Integer quantite;
    private String categorieName;
}
