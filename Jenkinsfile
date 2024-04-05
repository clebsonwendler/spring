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
                    git branch: "${BRANCH_NAME}", credentialsId: "github", url: "${GIT_URL}"
                }
            }
        }

        stage("SonarQube Analysis"){
            steps{
                script{
                    if(env.BRANCH_NAME == 'main'){
                        sh '''
                            echo SUCESSO develop!
                            env
                            echo $REPOSITORY_NAME
                            echo $REPOSITORY_OWNER
                        '''
                    }else if(env.BRANCH_NAME == 'develop'){
                        withSonarQubeEnv(credentialsId: 'sonarqube') {
                            sh './mvnw -Pdev sonar:sonar -Dsonar.projectName="nome-teste"'
                        }
                    }else if(env.BRANCH_NAME == 'staging'){
                        sh '''
                            echo SUCESSO staging!
                            env
                        '''
                    }
                }
            }
        }

        // stage('Update Deployment File') {
        //     steps {
        //         script {
        //             sh '''
        //                 cat index.html
        //                 sed -i "s|Hello.*|Hello-$BUILD_NUMBER|g" index.html
        //                 cat index.html
        //                 git config user.email "noc@certdox.io"
        //                 git config user.name "Jenkins Agent"
        //                 git add index.html
        //                 git commit -m "Update to version $RELEASE"
        //                 git push https://$GITHUB_TOKEN@github.com/$GITHUB_USERNAME/$REPO_NAME HEAD:$BRANCH_NAME
        //             '''
        //         }
        //     }
        // }
        // Mantenha o nome Jenkins Agent para que ocorra a validação no envio do webhook pelo github


    }
}