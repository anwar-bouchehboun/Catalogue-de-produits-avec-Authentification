package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.CategorieRequest;
import com.authentification.produit.auth.dto.response.CategorieRespo;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.entity.Categorie;
import com.authentification.produit.auth.mapper.CategorieMapper;
import com.authentification.produit.auth.mapper.ProduitMapper;
import com.authentification.produit.auth.repository.CategorieRepo;
import com.authentification.produit.auth.repository.ProduitRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class CategorieService {
    private final ProduitRepo produitRepo;

    private final CategorieRepo categorieRepo;
    private final CategorieMapper categorieMapper =CategorieMapper.INSTANCE;
    private final ProduitMapper produitMapper=ProduitMapper.INSTANCE;

    public CategorieRespo creer(CategorieRequest request) {

        Categorie categorie = categorieMapper.toEntity(request);
       return categorieMapper.toDto(categorieRepo.save(categorie));
    }
      public List<CategorieRespo> creerPlusieurs(List<CategorieRequest> requests) {
        List<Categorie> categories = requests.stream()
                .map(categorieMapper::toEntity)
                .collect(Collectors.toList());
        
        return categorieRepo.saveAll(categories)
                .stream()
                .map(categorieMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategorieRespo modifier(Long id, CategorieRequest request) {
        Categorie categorie = categorieRepo.findById(id)
            .orElseThrow(() -> new NotFoundExceptionHndler("Catégorie non trouvée"));
        categorieMapper.updateEntity(categorie, request);
        return categorieMapper.toDto(categorieRepo.save(categorie));
    }

    public void supprimer(Long id) {
        if (!categorieRepo.existsById(id)){
            throw new NotFoundExceptionHndler("Catégorie id non trouvée");
        }
        categorieRepo.deleteById(id);
    }

    @Transactional
    public CategorieRespo trouverParId(Long id) {
        return categorieRepo.findById(id)
            .map(categorieMapper::toDto)
            .orElseThrow(() -> new NotFoundExceptionHndler("Catégorie non trouvée"));
    }

    @Transactional
    public Page<CategorieRespo> lister(String recherche, Pageable pageable) {
        if (recherche != null && !recherche.isEmpty()) {
            return categorieRepo.findByNomContainingIgnoreCase(recherche, pageable)
                .map(categorieMapper::toDto);
        }
        return categorieRepo.findAll(pageable)
            .map(categorieMapper::toDto);
    }


    public Page<ProduitRespo> findByCategorie_Nom(List<String> nomCategorie, Pageable pageable) {
        return produitRepo.findByCategorie_Nom(nomCategorie, pageable)
                .map(produitMapper::toProduitRespo);
    }
}