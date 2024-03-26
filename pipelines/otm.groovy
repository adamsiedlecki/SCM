pipeline {
    agent any
    options {
        ansiColor('xterm') // lub 'xterm' lub 'truecolor'
    }
    stages {
        stage('Code Checkout'){
            steps {
                checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHubToken', url: 'https://github.com/adamsiedlecki/OTM.git']])
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Run Surefire Tests') {
            steps {
                sh "mvn surefire:test"
            }
        }
        stage('Run Failsafe Tests') {
            steps {
                sh "mvn failsafe:integration-test"
            }
        }
    }
}