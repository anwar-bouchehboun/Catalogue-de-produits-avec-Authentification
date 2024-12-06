package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.ProduitRequest;
import com.authentification.produit.auth.dto.response.ProduitRespo;
import com.authentification.produit.auth.entity.Categorie;
import com.authentification.produit.auth.entity.Produit;
import com.authentification.produit.auth.mapper.ProduitMapper;
import com.authentification.produit.auth.repository.CategorieRepo;
import com.authentification.produit.auth.repository.ProduitRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProduitServiceTest {

    @Mock
    private ProduitRepo produitRepo;

    @Mock
    private CategorieRepo categorieRepo;

    @Mock
    private ProduitMapper produitMapper;

    @InjectMocks
    private ProduitService produitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreerProduit() {
        ProduitRequest request = new ProduitRequest("Produit1", 100.0, 10, 1L);
        Categorie categorie = new Categorie();
        categorie.setId(1L);

        when(categorieRepo.findById(1L)).thenReturn(Optional.of(categorie));
        when(produitMapper.toProduit(request)).thenReturn(new Produit());
        when(produitRepo.save(any(Produit.class))).thenReturn(new Produit());
        when(produitMapper.toProduitRespo(any(Produit.class))).thenReturn(new ProduitRespo());

        ProduitRespo response = produitService.creer(request);

        assertNotNull(response);
        verify(produitRepo, times(1)).save(any(Produit.class));
    }

    @Test
    void testModifierProduitNotFound() {
        when(produitRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundExceptionHndler.class, () -> produitService.modifier(1L, new ProduitRequest()));
    }

    @Test
    void testListerProduits() {
        Pageable pageable = PageRequest.of(0, 10);
        when(produitRepo.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<ProduitRespo> produits = produitService.lister(pageable);

        assertNotNull(produits);
        assertEquals(0, produits.getTotalElements());
    }
}
