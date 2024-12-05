package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.ProduitRequest;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.entity.Categorie;
import com.authentification.produit.auth.repository.CategorieRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProduitServiceTestIntegration {
    
    @Autowired
    private ProduitService produitService;

    @Autowired
    private CategorieRepo categorieRepo;



    private Categorie categorie;
    private ProduitRequest produitRequest;

    @BeforeEach
    void setUp() {
        // Créer une catégorie de test
        categorie = new Categorie();
        categorie.setNom("Test Catégorie");
        categorie = categorieRepo.save(categorie);

        // Préparer une requête de produit
        produitRequest = new ProduitRequest();
        produitRequest.setDesignation("Test Produit");
        produitRequest.setPrix(100.0);
        produitRequest.setQuantite(10);
        produitRequest.setCategorieId(categorie.getId());
    }

    @Test
    void testCreerProduit() {
        ProduitRespo produitRespo = produitService.creer(produitRequest);

        assertNotNull(produitRespo);
        assertEquals("Test Produit", produitRespo.getDesignation());
        assertEquals(100.0, produitRespo.getPrix());
        assertEquals(10, produitRespo.getQuantite());
        assertEquals("Test Catégorie", produitRespo.getCategorieName());
    }

    @Test
    void testCreerPlusieursProduits() {
        ProduitRequest request2 = new ProduitRequest();
        request2.setDesignation("Test Produit 2");
        request2.setPrix(200.0);
        request2.setQuantite(20);
        request2.setCategorieId(categorie.getId());

        List<ProduitRequest> requests = Arrays.asList(produitRequest, request2);
        List<ProduitRespo> responses = produitService.creerPlusieurs(requests);

        assertEquals(2, responses.size());
        assertEquals("Test Produit", responses.get(0).getDesignation());
        assertEquals("Test Produit 2", responses.get(1).getDesignation());
    }

    @Test
    void testModifierProduit() {
        ProduitRespo produitCree = produitService.creer(produitRequest);
        
        produitRequest.setDesignation("Produit Modifié");
        ProduitRespo produitModifie = produitService.modifier(produitCree.getId(), produitRequest);

        assertEquals("Produit Modifié", produitModifie.getDesignation());
    }

    @Test
    void testSupprimerProduit() {
        ProduitRespo produitCree = produitService.creer(produitRequest);
        
        produitService.supprimer(produitCree.getId());
        
        assertThrows(NotFoundExceptionHndler.class, () -> {
            produitService.trouverParId(produitCree.getId());
        });
    }

    @Test
    void testListerProduits() {
        produitService.creer(produitRequest);
        
        Page<ProduitRespo> page = produitService.lister(PageRequest.of(0, 10));
        
        assertFalse(page.getContent().isEmpty());
    }

    @Test
    void testRechercherParDesignation() {
        produitService.creer(produitRequest);
        
        Page<ProduitRespo> page = produitService.rechercherParDesignation("Test", PageRequest.of(0, 10));
        
        assertFalse(page.getContent().isEmpty());
        assertTrue(page.getContent().get(0).getDesignation().contains("Test"));
    }

    @Test
    void testRechercherParNomCategorie() {
        produitService.creer(produitRequest);
        
        Page<ProduitRespo> page = produitService.rechercherParNomCategorie("Test", PageRequest.of(0, 10));
        
        assertFalse(page.getContent().isEmpty());
        assertEquals("Test Catégorie", page.getContent().get(0).getCategorieName());
    }
}
