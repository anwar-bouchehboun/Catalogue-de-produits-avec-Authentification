package com.authentification.produit.auth.mapper;

import com.authentification.produit.auth.dto.request.ProduitRequest;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.entity.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    ProduitMapper INSTANCE = Mappers.getMapper(ProduitMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "designation", target = "designation")
    @Mapping(source = "prix", target = "prix")
    @Mapping(source = "quantite", target = "quantite")
    @Mapping(source = "categorie.nom", target = "categorieName")
    ProduitRespo toProduitRespo(Produit produit);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "designation", target = "designation")
    @Mapping(source = "prix", target = "prix")
    @Mapping(source = "quantite", target = "quantite")
    @Mapping(target = "categorie", ignore = true)
    @Mapping(source = "categorieId", target = "categorie.id")
    Produit toProduit(ProduitRequest produitRequest);

    List<ProduitRespo> toProduitRespoList(List<Produit> produits);
    List<Produit> toProduitList(List<ProduitRequest> produitRequests);
}
