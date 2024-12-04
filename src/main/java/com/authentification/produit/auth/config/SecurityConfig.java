package com.authentification.produit.auth.config;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login as principal,password as credentials, active as enabled FROM users WHERE login = ?")
                .authoritiesByUsernameQuery(
                    "SELECT u.login as username, r.name as authority " +
                    "FROM users u " +
                    "JOIN user_roles ur ON u.id = ur.user_id " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE u.login = ?"
                )
                .passwordEncoder(passwordEncoder())
                .rolePrefix("ROLE_");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .sessionManagement()
            .maximumSessions(1)
            .and()
            .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**","/h2-console/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")  // Spring ajoutera automatiquement le préfixe ROLE_
                .antMatchers("/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .anyRequest().authenticated()

            .and()
            .httpBasic()
            .and()
            .formLogin()
            .failureUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    log.info("Authentification réussie pour : {}", authentication.getName());
                    log.info("Rôles : {}", authentication.getAuthorities());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":\"success\",\"sessionId\":\"" + request.getSession().getId() + "\"}");
                })
                .failureHandler((request, response, exception) -> {
                    log.error("Échec d'authentification : {}", exception.getMessage());
                    response.setStatus(401);
                    response.getWriter().write("{\"error\":\"Authentification échouée\"}");
                })
                  .and()
            .logout()
                .logoutUrl("/api/auth/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\":\"Déconnexion réussie\"}");
                    log.info("Utilisateur déconnecté avec succès");
                })
                .and()  
                .headers().frameOptions().sameOrigin() ;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
