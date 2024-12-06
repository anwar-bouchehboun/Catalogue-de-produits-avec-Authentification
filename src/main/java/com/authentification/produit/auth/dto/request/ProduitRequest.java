package com.authentification.produit.auth.dto.request;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRequest {


    @NotNull(message = "La désignation ne peut pas être nulle")
    @NotBlank(message = "La désignation ne peut pas être vide")
    @Size(min = 3, max = 100, message = "La désignation doit contenir entre 3 et 100 caractères")
    private String designation;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0", message = "Le prix doit être supérieur à 0")
    private Double prix;

    @NotNull(message = "La quantité ne peut pas être nulle")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantite;

    @NotNull(message = "La Categorie Id  ne peut pas être nulle")
    private Long categorieId;
}
