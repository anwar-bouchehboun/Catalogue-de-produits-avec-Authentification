package com.authentification.produit.auth.controllers.admin;

import com.authentification.produit.auth.dto.request.ProduitRequest;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.services.ProduitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/produits")
@RequiredArgsConstructor
@Slf4j
public class AdminProduitController {
    private final ProduitService produitService;

    @PostMapping
    public ResponseEntity<ProduitRespo> creer(@Valid @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.creer(request));
    }

    @PostMapping("/add-list")
    public ResponseEntity<List<ProduitRespo>> creerPlusieurs(@Valid @RequestBody List<ProduitRequest> requests) {
        return ResponseEntity.ok(produitService.creerPlusieurs(requests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitRespo> modifier(@PathVariable Long id, @Valid @RequestBody ProduitRequest request) {
        return ResponseEntity.ok(produitService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        produitService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitRespo> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.trouverParId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProduitRespo>> lister(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String recherche,
            @PageableDefault(size = 3) Pageable pageable) {

        if (categorie != null && !categorie.isEmpty()) {
            return ResponseEntity.ok(produitService.listerParCategorie(categorie, pageable));
        }
        if (recherche != null && !recherche.isEmpty()) {
            return ResponseEntity.ok(produitService.rechercherParDesignation(recherche, pageable));
        }
        return ResponseEntity.ok(produitService.lister(pageable));
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<ProduitRespo>> filtrerProduits(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String recherche,
            @PageableDefault(size = 3, sort = "designation") Pageable pageable) {
            
        return ResponseEntity.ok(
            produitService.findByCategorie_Nom(categories, pageable)
        );
    }

    @GetMapping("/recherche")
    public ResponseEntity<Page<ProduitRespo>> rechercherParNomCategorie(
            @RequestParam String nomCategorie,
            @PageableDefault(size = 3) Pageable pageable) {
        return ResponseEntity.ok(produitService.rechercherParNomCategorie(nomCategorie, pageable));
    }
}
