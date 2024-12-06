package com.authentification.produit.auth.mapper;

import com.authentification.produit.auth.dto.request.CategorieRequest;
import com.authentification.produit.auth.dto.response.CategorieRespo;
import com.authentification.produit.auth.entity.Categorie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategorieMapper {
    CategorieMapper INSTANCE = Mappers.getMapper(CategorieMapper.class);

    @Mapping(target = "produits", source = "produits")
    @Mapping(target = "produits.categorieName", ignore = true)
    CategorieRespo toDto(Categorie categorie);

    @Mapping(target = "produits", ignore = true)
    Categorie toEntity(CategorieRequest request);

    void updateEntity(@MappingTarget Categorie categorie, CategorieRequest request);
}
