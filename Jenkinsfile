pipeline {
    agent any
    
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    
    stages {
        stage('Vérification des outils') {
            steps {
                node {
                    sh '''
                        java -version
                        mvn -version
                    '''
                }
            }
        }
        
        stage('Checkout') {
            steps {
                node {
                    script {
                        // Configuration Git pour gérer les problèmes de connexion
                        sh '''
                            git config --global http.postBuffer 524288000
                            git config --global core.compression 0
                            git config --global http.sslVerify false
                        '''
                        
                        // Checkout avec retry
                        retry(3) {
                            checkout scm: [$class: 'GitSCM',
                                branches: [[name: '*/master']],
                                userRemoteConfigs: [[
                                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git',
                                    credentialsId: ''
                                ]]
                            ]
                        }
                    }
                }
            }
        }
        
        stage('Clean') {
            steps {
                node {
                    sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true clean'
                }
            }
        }
        
        stage('Tests') {
            steps {
                node {
                    sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                node {
                    sh 'mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true package -DskipTests'
                }
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
