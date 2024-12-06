pipeline {
    agent any
    
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    
    stages {
        stage('Vérification des outils') {
            steps {
                sh '''
                    java -version
                    mvn -version
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
        
        stage('Clean') {
            steps {
                sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true clean'
            }
        }
        
        stage('Tests') {
            steps {
                sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
    }
    
    post {
        always {
            deleteDir()
        }
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Échec du pipeline!'
        }
    }
}
