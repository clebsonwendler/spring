// Definir os seguintes parâmetros de execução nas configurações
// da pipeline: TENANCY_NAMESPACE, ENDPOINT_REGISTRY

pipeline{
    agent any
    tools{
        jdk "Java21"
        maven "Maven3"
    }
    environment {
	    APP_NAME = "${JOB_NAME}"
        RELEASE = "v${BUILD_NUMBER}"
        IMAGE_NAME = "${ENDPOINT_REGISTRY}" + "/" + "${TENANCY_NAMESPACE}" + "/" + "${APP_NAME}"
        GITHUB_TOKEN = credentials('github_token_clebson')
        BRANCH_NAME = "${GIT_BRANCH}".split('/').last()
        REPOSITORY_NAME = "${env.GIT_URL.tokenize('/')[3].split('\\.')[0]}"
        REPOSITORY_OWNER = "${env.GIT_URL.tokenize('/')[2]}"
    }
    stages{
        stage("Cleanup Workspace"){
                steps {
                    cleanWs()
                }
        }

        stage("Checkout Application"){
            steps {
                script{
                    git branch: "${BRANCH_NAME}", url: "${GIT_URL}"
                }
            }
        }

        stage("SonarQube Analysis"){
            steps{
                script{
                    if(env.BRANCH_NAME == 'main'){
                        withSonarQubeEnv(credentialsId: 'sonarqube') {
                            sh './mvnw -Pprod sonar:sonar'
                        }
                    }else if(env.BRANCH_NAME == 'develop'){
                        withSonarQubeEnv(credentialsId: 'sonarqube') {
                            sh 'chmod +x mvnw && ./mvnw -Pdev sonar:sonar -Dsonar.projectName="novo-nome"'
                        }
                    }else if(env.BRANCH_NAME == 'staging'){
                        withSonarQubeEnv(credentialsId: 'sonarqube') {
                            sh './mvnw -Pstg sonar:sonar'
                        }
                    }
                }
            }
        }

        stage("Quality Gate"){
            steps{
                script{
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
                }
            }
        }

    }
}