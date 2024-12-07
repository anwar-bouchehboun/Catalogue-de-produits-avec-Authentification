pipeline {
    agent any
    
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    
    environment {
        MAVEN_OPTS = '-Dhttps.protocols=TLSv1.2'
        SPRING_PROFILES_ACTIVE = 'prod'
        APP_NAME = 'auth-app'
        APP_PORT = '8086'
        DB_HOST = 'localhost'
        DB_PORT = '3306'
        DB_NAME = 'catalogue'
        DB_USER = credentials('DB_USER')
        DB_PASSWORD = credentials('DB_PASSWORD')
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
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/master']],
                    extensions: [[$class: 'CloneOption',
                        depth: 1,
                        noTags: true,
                        shallow: true,
                        timeout: 30
                    ]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git',
                        credentialsId: 'git-credentials'
                    ]]
                ])
            }
        }
        
        stage('Clean') {
            steps {
                sh 'mvn clean -Dspring.profiles.active=prod'
            }
        }
        
        stage('Tests') {
            steps {
                sh 'mvn test -Dspring.profiles.active=prod'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn package -DskipTests -Dspring.profiles.active=prod'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh '''
                        echo "Arrêt du conteneur existant s'il existe"
                        docker stop ${APP_NAME} || true
                        docker rm ${APP_NAME} || true
                        
                        echo "Construction de l'image Docker"
                        docker build -t ${APP_NAME}:latest .
                        
                        echo "Démarrage du nouveau conteneur"
                        docker run -d \
                            --name ${APP_NAME} \
                            -p ${APP_PORT}:8086 \
                            -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} \
                            -e SPRING_DATASOURCE_URL=jdbc:mariadb://${DB_HOST}:${DB_PORT}/${DB_NAME} \
                            -e SPRING_DATASOURCE_USERNAME=${DB_USER} \
                            -e SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD} \
                            --network host \
                            ${APP_NAME}:latest
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Échec du pipeline!'
        }
    }
}
