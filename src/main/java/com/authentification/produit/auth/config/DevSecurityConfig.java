package com.authentification.produit.auth.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@Slf4j
@EnableWebSecurity
@Profile("dev")
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("123456789")).roles("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("123456789")).roles("USER");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
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
                .formLogin().disable()
                .httpBasic()
                .and()
                .headers().frameOptions().sameOrigin() ;

        log.info("Configuration de sécurité en mode développement activée");

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
