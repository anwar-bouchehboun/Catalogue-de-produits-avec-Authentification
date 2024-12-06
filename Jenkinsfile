pipeline {
    agent any
    
    environment {
        GITHUB_CREDS = credentials('GITHUB_CREDENTIALS')
    }
    
    stages {
        stage('Vérification des outils') {
            steps {
                sh 'which git'
                sh 'git --version'
            }
        }
        
        stage('Checkout') {
            steps {
                git branch: 'master',
                    credentialsId: 'GITHUB_CREDENTIALS',
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
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
                    git commit -m "Update from Jenkins" || echo "No changes to commit"
                    git push https://${GITHUB_CREDS_USR}:${GITHUB_CREDS_PSW}@github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git master
                '''
            }
        }
    }
    
    post {
        always {
            deleteDir()
        }
        success {
            echo 'Pipeline réussi!'
        }
        failure {
            echo 'Pipeline échoué!'
        }
    }
}
