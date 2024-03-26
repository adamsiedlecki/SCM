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
                script {
                    def surefireResult = sh(script: 'mvn surefire:test', returnStatus: true)
                    if (surefireResult != 0) {
                        currentBuild.result = 'FAILED'
                        error "Surefire tests failed"
                    }
                }
            }
        }
        stage('Run Failsafe Tests') {
            steps {
                script {
                    def failsafeResult = sh(script: 'mvn failsafe:integration-test', returnStatus: true)
                    if (failsafeResult != 0) {
                        currentBuild.result = 'FAILED'
                        error "Failsafe tests failed"
                    }
                }
            }
        }
    }
}