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
        DB_HOST = 'host.docker.internal'
        DB_PORT = '3306'
        DB_NAME = 'catalogue'
        DATABASE_CREDS = credentials('mariadb-credentials')
        MAVEN_HOME = tool 'maven'
        PATH = "${MAVEN_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Vérification Maven') {
            steps {
                sh 'mvn --version'
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
                        url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
                    ]]
                ])
            }
        }
        
        stage('Clean') {
            steps {
                sh 'mvn -s settings.xml clean -Dspring.profiles.active=prod'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -s settings.xml package -DskipTests -Dspring.profiles.active=prod'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }

        stage('Tests') {
            steps {
                script {
                    sh '''
                        # Vérification de la connectivité à MariaDB
                        nc -zv host.docker.internal 3306 || true
                        
                        # Exécution des tests avec le profil test
                        mvn -s settings.xml test -Dspring.profiles.active=test \
                            -Dspring.datasource.url=jdbc:mariadb://host.docker.internal:3306/catalogue \
                            -Dspring.datasource.username=root \
                            -Dspring.datasource.password=root \
                            -Dspring.datasource.driver-class-name=org.mariadb.jdbc.Driver
                    '''
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(credentialsId: 'mariadb-credentials', 
                                      usernameVariable: 'DB_USER', 
                                      passwordVariable: 'DB_PASSWORD')
                    ]) {
                        sh '''
                            echo "Nettoyage des anciens conteneurs"
                            docker stop ${APP_NAME} mariadb phpmyadmin || true
                            docker rm ${APP_NAME} mariadb phpmyadmin || true
                            
                            echo "Construction de l'image Docker"
                            docker build -t ${APP_NAME}:latest .
                            
                            echo "Démarrage d'un nouveau conteneur MariaDB"
                            docker run -d \\
                                --name mariadb \\
                                -e MYSQL_ROOT_PASSWORD=root \\
                                -e MYSQL_DATABASE=${DB_NAME} \\
                                -p 3306:3306 \\
                                mariadb:latest
                            
                            echo "Démarrage de phpMyAdmin"
                            docker run -d \\
                                --name phpmyadmin \\
                                -e PMA_HOST=mariadb \\
                                -e PMA_USER=root \\
                                -e PMA_PASSWORD=root \\
                                -p 8080:80 \\
                                --link mariadb:db \\
                                phpmyadmin/phpmyadmin
                            
                            echo "Attente du démarrage de MariaDB"
                            sleep 20
                            
                            echo "Démarrage du nouveau conteneur"
                            docker run -d \\
                                --name ${APP_NAME} \\
                                -p ${APP_PORT}:8086 \\
                                -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} \\
                                -e SPRING_DATASOURCE_URL="jdbc:mariadb://mariadb:3306/${DB_NAME}?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false" \\
                                -e SPRING_DATASOURCE_USERNAME="root" \\
                                -e SPRING_DATASOURCE_PASSWORD="root" \\
                                --link mariadb:mariadb \\
                                ${APP_NAME}:latest
                            
                            echo "Attente du démarrage de l'application"
                            sleep 20
                            
                            echo "Accès à phpMyAdmin : http://localhost:8080"
                            echo "Utilisateur : root"
                            echo "Mot de passe : root"
                            
                            echo "Statut des conteneurs"
                            docker ps -a
                        '''
                    }
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
