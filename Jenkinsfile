pipeline {
    agent any
    tools { 
        maven 'maven 3.5.4' 
        jdk 'jdk8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }
        }

        stage ('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.war', fingerprint: true
        }
    }
}