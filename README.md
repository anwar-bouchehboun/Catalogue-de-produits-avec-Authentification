### 📦 Gestion des Produits et Catégories

## Contexte du Projet

- En tant que Développeur Full Stack, vous devez sécuriser les API via l'authentification stateful, conteneuriser l'application avec Docker et l'intégrer avec Jenkins pour le déploiement continu. Vous serez également responsable de maintenir la documentation technique et de former les équipes.

## 🛠️ Technologies et Concepts Utilisés

- Spring Boot : Framework principal pour le développement des APIs REST.

- Spring Data JPA : Pour la gestion des données avec MariaDB/OracleXE.

- Spring Security : Authentification et autorisation sécurisées.

- Jenkins : Intégration et déploiement continu.

- Docker : Conteneurisation de l'application.

- JUnit & Mockito : Tests unitaires et d'intégration.

- Swagger : Documentation des APIs.

- Lombok : Simplification du code avec des annotations.

- Git : Gestion de version avec branchement (Git Flow).

- SonarLint : Analyse statique du code pour assurer sa qualité.

## 📂 Structure des Entités

- **Produit** : Entité principale pour les produits.
  - designation (String)
  - prix (Double)
  - quantite (BigDecimal)
- **Categorie** : Entité pour les catégories.

  - nom (String)
  - ProduitCategorie : Entité pour la relation entre les produits et les catégorie
  - description (String)

- **User** : Entité pour les User

  - login (String)
  - password (String)
  - active (Boolean)

- **Roles** (Collection de Role)
  - id (Long)
  - name (String)

## 🔒 Sécurité

- Authentification Stateful via Spring Security avec JdbcAuthentication.

- Autorisation via les rôles (Role) et les permissions (Permission) associées.

- Les URLs /api/admin/ nécessitent un rôle ADMIN.

- Les URLs /api/user/ nécessitent un rôle USER.

- Utilisation de BCryptPasswordEncoder pour le cryptage des mots de passe.

## 🔐 Tests d'authentification

- Pour tester via Postman, il est nécessaire de s'authentifier.

- Utilisation de JUnit et Mockito pour les tests unitaires et d'intégration.

- Configuration pour dev : Désactivation de la sécurité pour les tests.

- Configuration pour prod : Authentification via JdbcAuthentication.

## 🧑‍💻 Fonctionnalités

#### 📦 Gestion des Produits

###### - Lister les produits avec pagination.

###### - Rechercher les produits par désignation.

###### - Filtrer les produits par catégorie.

###### - Ajouter, modifier, supprimer des produits (ADMIN uniquement).

### Endpoints :

```
/api/user/products/**
/api/admin/products/**
```

#### 🏷️ Gestion des Catégories

###### - Lister les catégories avec pagination.

###### - Rechercher les catégories par nom.

###### - Lister les produits d'une catégorie.

###### - Ajouter, modifier, supprimer des catégories (ADMIN uniquement).

#### Endpoints :

```
/api/user/categories/**
/api/admin/categories/**
```

#### 👥 Gestion des Utilisateurs

###### - Authentification : /api/auth/login

###### - Inscription : POST /api/auth/register

###### - Liste des utilisateurs (ADMIN uniquement) : GET /api/admin/users

##### - Gestion des rôles des utilisateurs (ADMIN uniquement) : PUT /api/admin/users/{id}/roles

#### 🛠️ Couches Applicatives

- Controller : Gestion des requêtes HTTP.

- Service : Logique métier.

- Repository : Accès aux données avec Spring Data JPA.

- DTO : Data Transfer Object pour les échanges.

- Mapper : Conversion entre entités et DTO.

- Exception : Gestion des erreurs personnalisées.

- Validation : Bean Validation et validations métier.

- Utils : Fonctions utilitaires.

- Tests : Tests unitaires et d'intégration.

- 📦 CI/CD et Conteneurisation avec Docker

- Dockerfile

- dockerfile

# Commande pour exécuter l'application

```
CMD ["java", "-jar", "gestion-produit.jar"]
```

Jenkins

- Pipeline Jenkins pour l'intégration et le déploiement continus.
- Configuration pour les tests automatiques et le déploiement sur serveur.

### ⚙️ Profils

- 📡 Profil Développement (dev)
- 🔐 Profil Production (prod)
- JdbcAuthentication pour la gestion des utilisateurs et des rôles via la base de données.
- 📋 Tests Unitaires et d'Intégration
- Utilisation de JUnit et Mockito pour garantir la qualité du code.
- Tests d'API avec Postman pour vérifier les scénarios de sécurité.

#### 🛠️ Outils de Travail

- Git pour la gestion de versions et l'intégration avec GitHub.
- JIRA pour la gestion de projet agile avec Scrum.
- SonarLint pour assurer la qualité du code.
- Lombok pour réduire la verbosité du code.

### 🚀 Lancer l'Application

- Via Docker :

```
 docker run -p 8080:8080 gestion-produit
```

Via Spring Boot :

- Lancer l'application avec Maven :

```
bash :

mvn spring-boot:run
```

### 📜 Documentation API

- La documentation des API est disponible via Swagger à l'URL suivante :
  Swagger API Documentation
