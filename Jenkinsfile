pipeline {
    agent any
    stages {
        stage('Java/Maven Version') {
           steps {
               sh 'echo $PATH'
               sh 'echo $JAVA_HOME'
               sh 'java -version'
               sh 'javac -version'
               sh 'mvn -version'
           }
       }
       stage('Build') {
           steps {
               sh 'mvn -e -X -B -DskipTests clean install'
           }
       }
       stage('Test') {
           steps {
               sh 'mvn test'
           }
           post {
               always {
                   junit 'target/surefire-reports/*.xml'
               }
           }
       }
   }
}