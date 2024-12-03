package com.authentification.produit.auth.controllers.admin;

import com.authentification.produit.auth.dto.request.CategorieRequest;
import com.authentification.produit.auth.dto.response.CategorieRespo;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.services.CategorieService;
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
@Slf4j
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategorieController {
    private final CategorieService categorieService;

    @PostMapping
    public ResponseEntity<CategorieRespo> creer(@Valid @RequestBody CategorieRequest request) {
        return ResponseEntity.ok(categorieService.creer(request));
    }

    @PostMapping("/add-list")

    public ResponseEntity<List<CategorieRespo>> creerPlusieurs(
            @Valid @RequestBody List<CategorieRequest> requests) {
        return ResponseEntity.ok(categorieService.creerPlusieurs(requests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieRespo> modifier(@Valid @PathVariable Long id, @RequestBody CategorieRequest request) {
        return ResponseEntity.ok(categorieService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        categorieService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieRespo> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.trouverParId(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategorieRespo>> lister(
            @RequestParam(required = false) String recherche,
            @PageableDefault(size = 3) Pageable pageable) {
        return ResponseEntity.ok(categorieService.lister(recherche, pageable));
    }
        @GetMapping("/produits")
    public ResponseEntity<Page<ProduitRespo>> findByCategorie_Nom(
            @RequestParam List<String> nomCategorie,
            @PageableDefault(size = 10, sort = "designation") Pageable pageable) {
        return ResponseEntity.ok(categorieService.findByCategorie_Nom(nomCategorie, pageable));
    }
}
