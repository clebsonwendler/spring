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

        stage('Print Value') {
            steps {
                script {
                    sh'''
                        echo ${params.payload}
                        env
                    '''
                }
            }
        }

        stage("Update Version for ArgoCD") {
            steps {
                script {
                    if(env.BRANCH_NAME == 'main'){
                        sh '''
                            cat manifests/prod/deployment.yaml
                            sed -i "s|$IMAGE_NAME:.*|$IMAGE_NAME:$BRANCH_NAME-$RELEASE|g" manifests/prod/deployment.yaml
                            cat manifests/prod/deployment.yaml
                        '''
                    }else if(env.BRANCH_NAME == 'develop'){
                        sh '''
                            cat manifests/dev/deployment.yaml
                            sed -i "s|$IMAGE_NAME:.*|$IMAGE_NAME:$BRANCH_NAME-$RELEASE|g" manifests/dev/deployment.yaml
                            cat manifests/dev/deployment.yaml
                        '''
                    }else if(env.BRANCH_NAME == 'staging'){
                        sh '''
                            cat manifests/stg/deployment.yaml
                            sed -i "s|$IMAGE_NAME:.*|$IMAGE_NAME:$BRANCH_NAME-$RELEASE|g" manifests/stg/deployment.yaml
                            cat manifests/stg/deployment.yaml
                        '''
                    }
                }
            }
        }

        // stage('Update Deployment File') {
        //     steps {
        //         script {
        //             if(env.BRANCH_NAME == 'main'){
        //                 sh '''
        //                     git config user.email "noc@certdox.io"
        //                     git config user.name "Jenkins Agent"
        //                     git add manifests/prod/deployment.yaml
        //                     git commit -m "Update to version $RELEASE"
        //                     git push https://$GITHUB_TOKEN@github.com/$REPOSITORY_OWNER/$REPOSITORY_NAME HEAD:$BRANCH_NAME
        //                 '''
        //             }else if(env.BRANCH_NAME == 'develop'){
        //                 sh '''
        //                     git config user.email "noc@certdox.io"
        //                     git config user.name "Jenkins Agent"
        //                     git add manifests/dev/deployment.yaml
        //                     git commit -m "Update to version $RELEASE"
        //                     git push https://$GITHUB_TOKEN@github.com/$REPOSITORY_OWNER/$REPOSITORY_NAME HEAD:$BRANCH_NAME
        //                 '''
        //             }else if(env.BRANCH_NAME == 'staging'){
        //                 sh '''
        //                     git config user.email "noc@certdox.io"
        //                     git config user.name "Jenkins Agent"
        //                     git add manifests/stg/deployment.yaml
        //                     git commit -m "Update to version $RELEASE"
        //                     git push https://$GITHUB_TOKEN@github.com/$REPOSITORY_OWNER/$REPOSITORY_NAME HEAD:$BRANCH_NAME
        //                 '''
        //             }
        //         }
        //     }
        // }

    }
}