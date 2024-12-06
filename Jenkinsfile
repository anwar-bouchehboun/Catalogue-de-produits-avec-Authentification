pipeline {
    agent any
    
    tools {
        jdk 'JDK'
        maven 'maven'
    }
    
    environment {
        MAVEN_OPTS = '-Dhttps.protocols=TLSv1.2'
        SPRING_PROFILES_ACTIVE = 'dev'

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
                    url: 'https://github.com/anwar-bouchehboun/Catalogue-de-produits-avec-Authentification.git'
            }
        }
        
        stage('Clean') {
            steps {
                script {
                    writeFile file: 'settings.xml', text: '''<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                            https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <mirror>
            <id>central-https</id>
            <name>Maven Central</name>
            <url>https://repo.maven.apache.org/maven2</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
</settings>'''
                    
                    // Forcer la version du plugin clean
                    writeFile file: 'pom.xml', text: readFile('pom.xml').replaceAll(
                        '</properties>',
                        '''    <maven-clean-plugin.version>3.1.0</maven-clean-plugin.version>
                        </properties>'''
                    )
                    
                    sh 'mvn -s settings.xml clean'
                }
            }
        }
        
        stage('Tests') {
            steps {
                sh 'mvn -s settings.xml test -Dspring.profiles.active=dev'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -s settings.xml package -DskipTests -Dspring.profiles.active=dev'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
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
