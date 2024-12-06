pipeline {
    agent any
    
    environment {
        GITHUB_CREDS = credentials('GITHUB_CREDENTIALS')
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    credentialsId: 'GITHUB_CREDENTIALS',
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
            }
        }
        
        stage('Install Dependencies') {
            steps {
                sh '''
                    npm install
                    composer install
                '''
            }
        }
        
        stage('Build') {
            steps {
                sh '''
                    npm run build
                    php artisan key:generate
                    php artisan migrate
                '''
            }
        }
        
        stage('Test') {
            steps {
                sh '''
                    npm run test
                    php artisan test
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    php artisan config:cache
                    php artisan route:cache
                    php artisan view:cache
                '''
            }
        }
        
        stage('Push Changes') {
            steps {
                sh '''
                    git config --global user.email "anouar.ab95@gmail.com"
                    git config --global user.name "anwar-bouchehboun"
                    git add .
                    git commit -m "Build: Jenkins deployment" || echo "No changes to commit"
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
            echo 'Pipeline terminé avec succès!'
            sh '''
                echo "Build #${BUILD_NUMBER} - Success" >> build_history.txt
            '''
        }
        failure {
            echo 'Pipeline échoué!'
            sh '''
                echo "Build #${BUILD_NUMBER} - Failed" >> build_history.txt
            '''
        }
    }
}
