package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.ProduitRequest;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.entity.Categorie;
import com.authentification.produit.auth.entity.Produit;
import com.authentification.produit.auth.mapper.ProduitMapper;
import com.authentification.produit.auth.repository.CategorieRepo;
import com.authentification.produit.auth.repository.ProduitRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {
    private final ProduitRepo produitRepo;
    private final CategorieRepo categorieRepo;
    private final ProduitMapper produitMapper=ProduitMapper.INSTANCE;

    public ProduitRespo creer(ProduitRequest request) {
        Categorie categorie = categorieRepo.findById(request.getCategorieId())
                .orElseThrow(() -> new NotFoundExceptionHndler("Catégorie non trouvée"));

        Produit produit = produitMapper.toProduit(request);
        produit.setCategorie(categorie);
        return produitMapper.toProduitRespo(produitRepo.save(produit));
    }

    public List<ProduitRespo> creerPlusieurs(List<ProduitRequest> requests) {
        List<Produit> produits = produitMapper.toProduitList(requests);
        produits.forEach(produit -> {
            Categorie categorie = categorieRepo.findById(produit.getCategorie().getId())
                    .orElseThrow(() -> new NotFoundExceptionHndler("Catégorie non trouvée"));
            produit.setCategorie(categorie);
        });
        return produitMapper.toProduitRespoList(produitRepo.saveAll(produits));
    }

    public ProduitRespo modifier(Long id, ProduitRequest request) {
        Produit produit = produitRepo.findById(id)
                .orElseThrow(() -> new NotFoundExceptionHndler("Produit non trouvé"));

        Categorie categorie = categorieRepo.findById(request.getCategorieId())
                .orElseThrow(() -> new NotFoundExceptionHndler("Catégorie non trouvée"));

        produit.setDesignation(request.getDesignation());
        produit.setPrix(request.getPrix());
        produit.setQuantite(request.getQuantite());
        produit.setCategorie(categorie);

        return produitMapper.toProduitRespo(produitRepo.save(produit));
    }

    public void supprimer(Long id) {
        if (!produitRepo.existsById(id)) {
            throw new NotFoundExceptionHndler("Produit non trouvé");
        }
        produitRepo.deleteById(id);
    }

    public ProduitRespo trouverParId(Long id) {
        return produitRepo.findById(id)
                .map(produitMapper::toProduitRespo)
                .orElseThrow(() -> new NotFoundExceptionHndler("Produit non trouvé"));
    }

    public Page<ProduitRespo> lister(  Pageable pageable) {
        return produitRepo.findAll(pageable)
                .map(produitMapper::toProduitRespo);
    }

    public Page<ProduitRespo> listerParCategorie(String nomCategorie, Pageable pageable) {
        if (nomCategorie != null && !nomCategorie.isEmpty()) {
            return produitRepo.findByCategorie(nomCategorie, pageable)
                    .map(produitMapper::toProduitRespo);
        }
        return produitRepo.findAll(pageable)
                .map(produitMapper::toProduitRespo);
    }

    public Page<ProduitRespo> rechercherParDesignation(String designation, Pageable pageable) {
        return produitRepo.findByDesignationContainingIgnoreCase(designation, pageable)
                .map(produitMapper::toProduitRespo);
    }

    public Page<ProduitRespo> rechercherParNomCategorie(String nomCategorie, Pageable pageable) {
        return produitRepo.findByCategorieNomContainingIgnoreCase(nomCategorie, pageable)
                .map(produitMapper::toProduitRespo);
    }

    public Page<ProduitRespo> findByCategorie_Nom( List<String> nomCategorie,Pageable pageable) {
        return produitRepo.findByCategorie_Nom(nomCategorie, pageable)
                .map(produitMapper::toProduitRespo);
    }


}
