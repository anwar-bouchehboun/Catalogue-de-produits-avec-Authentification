pipeline {
    agent any
    
    environment {
        // Utilisation des identifiants GitHub
        GITHUB_CREDS = credentials('GITHUB_CREDENTIALS')
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Récupération du code
                git branch: 'master',
                    credentialsId: 'GITHUB_CREDENTIALS',
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
            }
        }
        
        stage('Build') {
            steps {
                // Étapes de build
                sh 'npm install'
                sh 'npm run build'
            }
        }
        
        stage('Test') {
            steps {
                // Étapes de test
                sh 'npm test'
            }
        }
        
        stage('Deploy') {
            steps {
                // Déploiement
                sh """
                    git config user.email "votre-email@example.com"
                    git config user.name "anwar-bouchehboun"
                    git add .
                    git commit -m "Deploy: Jenkins build"
                    git push https://${GITHUB_CREDS_USR}:${GITHUB_CREDS_PSW}@github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git
                """
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Pipeline échoué!'
        }
    }
}
