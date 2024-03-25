node(){

    stage('Code Checkout'){
        checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHubToken', url: 'https://github.com/adamsiedlecki/OTM.git']])
    }
    stage('Build') {
        sh "mvn clean package"
    }
    stage('Run Surefire Tests') {
        sh "mvn surefire:test"
    }

    stage('Run Failsafe Tests') {
        sh "mvn failsafe:integration-test"
    }


}
