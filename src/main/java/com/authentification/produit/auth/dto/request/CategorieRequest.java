package com.authentification.produit.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategorieRequest {

    @NotNull
    @NotBlank(message = "Le nom Categorie est obligatoire.")
    @Size(min = 5, max = 50)
    private String nom;

    @Size(max = 255)
    @NotNull
    @NotBlank(message = "Le description  est obligatoire.")
    private String description;
}
