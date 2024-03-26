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
                echo "STARTING BUILD"
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Run Surefire Tests') {
            steps {
                echo "STARTING SUREFIRE"
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
                echo "STARTING FAILSAE"
                script {
                    def failsafeResult = sh(script: 'mvn failsafe:integration-test', returnStatus: true)
                    echo "FAILSAFE RETURNED CODE: ${failsafeResult}"
                    if (failsafeResult != 0) {
                        currentBuild.result = 'FAILED'
                        error "Failsafe tests failed"
                    }
                }
            }
        }
    }
}