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
                    git branch: "${branchName}", credentialsId: "github", url: "${GIT_URL}"
                }
            }
        }

        stage("SonarQube Analysis"){
            steps{
                script{
                    def gitUrl = env.GIT_URL
                    // Extrair o nome de usuário
                    def username = gitUrl.tokenize('/')[3]

                    // Extrair o nome do repositório
                    def repository = gitUrl.tokenize('/')[4]

                    if(env.BRANCH_NAME == 'main'){
                        sh '''
                            echo SUCESSO main!
                            env
                        '''
                    }else if(env.BRANCH_NAME == 'develop'){
                        sh '''
                            echo SUCESSO develop!
                            env
                            
                            echo $username
                            echo $repository
                        '''
                    }else if(env.BRANCH_NAME == 'staging'){
                        sh '''
                            echo SUCESSO staging!
                            env
                        '''
                    }
                }
            }
        }

        stage('Update Deployment File') {
            steps {
                script {
                    sh '''
                        cat index.html
                        sed -i "s|Hello.*|Hello-$BUILD_NUMBER|g" index.html
                        cat index.html
                        git config user.email "noc@certdox.io"
                        git config user.name "Jenkins Agent"
                        git add index.html
                        git commit -m "Update to version $RELEASE"
                        git push https://$GITHUB_TOKEN@github.com/$GITHUB_USERNAME/$REPO_NAME HEAD:$BRANCH_NAME
                    '''
                }
            }
        }
        // Mantenha o nome Jenkins Agent para que ocorra a validação no envio do webhook pelo github


    }
}