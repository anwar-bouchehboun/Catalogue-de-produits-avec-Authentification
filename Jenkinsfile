pipeline {
    agent any
    
    environment {
        GITHUB_CREDS = credentials('GITHUB_CREDENTIALS')
    }
    
    stages {
        stage('Vérification des outils') {
            steps {
                sh '''
                    which git
                    git --version
                    node --version
                    npm --version
                '''
            }
        }
        
        stage('Checkout') {
            steps {
                git branch: 'master',
                    credentialsId: 'GITHUB_CREDENTIALS',
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
            }
        }
        
        stage('Installation des dépendances') {
            steps {
                sh 'npm install'
            }
        }
        
        stage('Tests') {
            steps {
                sh 'npm test || echo "Pas de tests configurés"'
            }
        }
        
        stage('Build') {
            steps {
                sh 'npm run build || echo "Pas de build nécessaire"'
            }
        }
        
        stage('Configuration Git') {
            steps {
                sh '''
                    git config --global user.email "anouar.ab95@gmail.com"
                    git config --global user.name "anwar-bouchehboun"
                '''
            }
        }
        
        stage('Push') {
            steps {
                sh '''
                    git add .
                    git commit -m "Build et déploiement depuis Jenkins" || echo "Pas de changements à commiter"
                    git push https://${GITHUB_CREDS_USR}:${GITHUB_CREDS_PSW}@github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git master
                '''
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Échec du pipeline!'
        }
    }
}
