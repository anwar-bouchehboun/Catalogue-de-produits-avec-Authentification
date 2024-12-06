pipeline {
    agent any
    
    environment {
        GITHUB_CREDS = credentials('GITHUB_CREDENTIALS')
        GIT_SSL_NO_VERIFY = 'true'
    }
    
    stages {
        stage('Checkout') {
            steps {
                sh 'git config --global http.sslVerify false'
                git branch: 'master',
                    credentialsId: 'GITHUB_CREDENTIALS',
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
            }
        }
        
        stage('Set Permissions') {
            steps {
                sh '''
                    chmod +x mvnw
                    chmod +x .mvn/wrapper/maven-wrapper.jar
                '''
            }
        }
        
        stage('Build Maven') {
            steps {
                sh '''
                    ./mvnw clean install -DskipTests
                    ./mvnw package -DskipTests
                '''
            }
        }
        
        stage('Test') {
            steps {
                sh '''
                    ./mvnw test
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    chmod +x target/*.jar
                    java -jar target/*.jar &
                    echo $! > .pidfile
                '''
            }
        }
        
        stage('Push Changes') {
            steps {
                sh '''
                    git config --global http.postBuffer 524288000
                    git config --global core.compression 0
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
            sh '''
                if [ -f .pidfile ]; then
                    kill $(cat .pidfile) || true
                    rm .pidfile
                fi
            '''
            deleteDir()
        }
        success {
            echo 'Pipeline terminé avec succès!'
        }
        failure {
            echo 'Pipeline échoué!'
        }
    }
}
