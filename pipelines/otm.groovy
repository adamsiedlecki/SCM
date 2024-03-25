pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Krok pobierania projektu z GitHuba
                script {
                    git 'https://github.com/adamsiedlecki/OTM.git'
                }
            }
        }

        stage('Build') {
            steps {
                // Krok budowania projektu Maven
                script {
                    sh "mvn clean package"
                }
            }
        }

        stage('Run Surefire Tests') {
            steps {
                // Krok uruchamiający testy Surefire
                script {
                    sh "mvn surefire:test"
                }
            }
        }

        stage('Run Failsafe Tests') {
            steps {
                // Krok uruchamiający testy Failsafe
                script {
                    sh "mvn failsafe:integration-test"
                }
            }
        }
    }
}