package com.authentification.produit.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;



    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminEndpointsWithAdminRole() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//check Admin
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAdminEndpointsWithUserRole() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//check User
        mockMvc.perform(post("/api/admin/categories/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserEndpointsWithUserRole() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//check User
        mockMvc.perform(get("/api/user/categories"))
                .andExpect(status().isOk());
    }
}
