package com.authentification.produit.auth.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieRequest {

    @NotNull(message = "Le nom ne peut pas être nul")
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 5, max = 50, message = "Le nom doit contenir entre 5 et 50 caractères")
    private String nom;

    @NotNull(message = "La description ne peut pas être nulle")
    @NotBlank(message = "La description ne peut pas être vide")
    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;
}
