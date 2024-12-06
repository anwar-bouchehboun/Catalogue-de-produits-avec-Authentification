pipeline {
    agent any
    
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    
    environment {
        MAVEN_OPTS = '-Dhttps.protocols=TLSv1.2 -Dmaven.repo.local=$HOME/.m2/repository -Duser.home=$HOME'
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
                script {
                    sh '''
                        git config --global http.postBuffer 524288000
                        git config --global core.compression 0
                        git config --global http.sslVerify false
                    '''
                    
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
        
        stage('Clean') {
            steps {
                sh '''
                    mvn clean \
                    -Dmaven.wagon.http.ssl.insecure=true \
                    -Dmaven.wagon.http.ssl.allowall=true \
                    -Dmaven.wagon.http.pool=false \
                    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
                    -Dmaven.repo.local=$HOME/.m2/repository
                '''
            }
        }
        
        stage('Tests') {
            steps {
                sh '''
                    mvn test \
                    -Dmaven.wagon.http.ssl.insecure=true \
                    -Dmaven.wagon.http.ssl.allowall=true \
                    -Dmaven.wagon.http.pool=false \
                    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
                    -Dmaven.repo.local=$HOME/.m2/repository
                '''
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh '''
                    mvn package -DskipTests \
                    -Dmaven.wagon.http.ssl.insecure=true \
                    -Dmaven.wagon.http.ssl.allowall=true \
                    -Dmaven.wagon.http.pool=false \
                    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
                    -Dmaven.repo.local=$HOME/.m2/repository
                '''
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
