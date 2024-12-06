package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.CategorieRequest;
import com.authentification.produit.auth.dto.response.CategorieRespo;
import com.authentification.produit.auth.entity.Categorie;
import com.authentification.produit.auth.repository.CategorieRepo;
import com.authentification.produit.auth.validation.NotFoundExceptionHndler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategorieServiceTest {

    @Mock
    private CategorieRepo categorieRepo;

    @InjectMocks
    private CategorieService categorieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreer() {
        CategorieRequest request = new CategorieRequest("Nom", "Description");
        Categorie categorie = new Categorie();
        categorie.setNom("Nom");
        categorie.setDescription("Description");

        when(categorieRepo.save(any(Categorie.class))).thenReturn(categorie);

        CategorieRespo response = categorieService.creer(request);

        assertEquals("Nom", response.getNom());
        verify(categorieRepo, times(1)).save(any(Categorie.class));
    }

    @Test
    public void testModifierNotFound() {
        when(categorieRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundExceptionHndler.class, () -> {
            categorieService.modifier(1L, new CategorieRequest("Nom", "Description"));
        });
    }
}
