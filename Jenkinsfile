pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './gradlew clean build'
      }
    }

    stage('upload') {
      steps {
        sh 'aws s3 cp build/libs/application.war s3://jenkins-ren2u/application.war --region ap-northeast-2'
      }
    }

    stage('deploy') {
      steps {
        sh 'aws elasticbeanstalk create-application-version --region ap-northeast-2 --application-name ren2u-cicd --version-label ${BUILD_TAG} --source-bundle S3Bucket="jenkins-ren2u",S3Key="application.war"'
        sh 'aws elasticbeanstalk update-environment --region ap-northeast-2 --environment-name Ren2ucicd-server --version-label ${BUILD_TAG}'
      }
    }

  }
}