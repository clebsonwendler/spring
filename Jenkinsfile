// Definir os parâmetros para execução nas configurações da pipeline
// TENANCY_NAMESPACE, ENDPOINT_REGISTRY, GITHUB_USERNAME, REPO_NAME

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
        GITHUB_TOKEN = credentials('github_token')
        BRANCH_NAME = "${GIT_BRANCH}".split('/').last()
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
                    def branchName = "${GIT_BRANCH}".split('/').last()
                    git branch: "${branchName}", credentialsId: "github", url: "https://github.com/${GITHUB_USERNAME}/${REPO_NAME}"
                }
            }
        }

        stage("SonarQube Analysis"){
            steps{
                script{
                    sh '''
                        echo SUCESSO!
                        env
                    '''
                }
            }
        }

    }
}