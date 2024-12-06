package com.authentification.produit.auth.services;

import com.authentification.produit.auth.dto.request.CategorieRequest;
import com.authentification.produit.auth.dto.response.CategorieRespo;
import com.authentification.produit.auth.repository.CategorieRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CategorieServiceIntegrationTest {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private CategorieRepo categorieRepo;

    @Test
    public void testCreerIntegration() {
        CategorieRequest request = new CategorieRequest("Categorie", "Description");
        CategorieRespo response = categorieService.creer(request);

        assertNotNull(response.getId());
        assertNotNull(categorieRepo.findById(response.getId()));
    }
}
