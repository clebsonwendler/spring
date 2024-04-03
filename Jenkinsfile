pipeline{
    agent any
    tools{
        jdk "Java21"
        maven "Maven3"
        nodejs "NodeJS20"
    }
    environment {
	    APP_NAME = "fromtisws"
        RELEASE = "1.0.0"
        TENANCY_NAMESPACE = "greggn67nu05"
        ENDPOINT_REGISTRY = "gru.ocir.io"
        IMAGE_NAME = "${ENDPOINT_REGISTRY}" + "/" + "${TENANCY_NAMESPACE}" + "/" + "${APP_NAME}"
        GITHUB_USERNAME = "certdox-io"
        REPO_NAME = "cdx-fromtis-ws"
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
            when { branch 'develop' }
            steps{
                script{
                    withSonarQubeEnv(credentialsId: 'sonarqube') {

                        if (env.BRANCH_NAME == 'main') {
                            def profile = 'prod'
                        } else if (env.BRANCH_NAME == 'staging'){
                            def profile = 'stg'
                        } else if (env.BRANCH_NAME == 'develop'){
                            def profile = 'dev'
                        }

                        sh 'echo $profile'

                        sh './mvnw -P${profile} clean verify -DskipTests sonar:sonar'
                    }
                }
            }
        }

    //     stage("Quality Gate"){
    //         steps{
    //             script{
    //                 waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
    //             }
    //         }
    //     }

    //     stage("Build Application"){
    //         steps {
    //             script{
    //                 def branchName = "${GIT_BRANCH}".split('/').last()

    //                 if (branchName == 'main') {
    //                     profile = 'prod'
    //                 } else if (branchName == 'staging'){
    //                     profile = 'stg'
    //                 } else if (branchName == 'develop'){
    //                     profile = 'dev'
    //                 }
                    
    //                 sh './mvnw -B dependency:go-offline'
    //                 sh './mvnw -P${profile} package verify -DskipTests'
    //             }
    //         }
    //     }

    //     stage("Run Custom Docker Daemon"){
    //         steps {
    //             sh 'sudo dockerd &'
    //         }
    //     }

    //     stage("Build Image") {
    //         steps{
    //             sh 'docker build -t ${IMAGE_NAME}:${RELEASE} .'
    //         }
    //     }

    //     stage("Trivy Scan") {
    //        steps {
    //             script {
	//                 sh 'docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${IMAGE_NAME}:${RELEASE} --no-progress --scanners vuln --exit-code 0 --severity HIGH,CRITICAL --format table'
    //            }
    //        }
    //     }

    //     stage("Build & Push Docker Image") {
    //         steps {
    //             withCredentials([usernamePassword(credentialsId: 'ociregistry', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    //                 script{

    //                     def branchName = "${GIT_BRANCH}".split('/').last()
                        
    //                     if (branchName == 'main') {
    //                         profile = 'prod'
    //                     } else if (branchName == 'staging'){
    //                         profile = 'stg'
    //                     } else if (branchName == 'develop'){
    //                         profile = 'dev'
    //                     }

    //                     sh '''
    //                         echo \$PASS | docker login --username \$USER --password-stdin $ENDPOINT_REGISTRY
    //                         docker tag $IMAGE_NAME:$RELEASE $IMAGE_NAME:$profile-$RELEASE
    //                         docker push $IMAGE_NAME:$profile-$RELEASE
    //                     '''
    //                 }
    //             }
    //         }
    //     }

    //    stage ("Cleanup Artifacts") {
    //        steps {
    //            script {
    //                 sh 'docker rmi $(docker image ls -aq)'
    //            }
    //       }
    //     }

    //     stage("Update Version for ArgoCD") {
    //         steps {
    //             script {

    //                 def branchName = "${GIT_BRANCH}".split('/').last()
                        
    //                 if (branchName == 'main') {
    //                     profile = 'prod'
    //                 } else if (branchName == 'staging'){
    //                     profile = 'stg'
    //                 } else if (branchName == 'develop'){
    //                     profile = 'dev'
    //                 }

    //                 sh '''
    //                     cat manifests/dev/deployment.yaml
    //                     sed -i "s|$IMAGE_NAME:.*|$IMAGE_NAME:$profile-$RELEASE|g" manifests/dev/deployment.yaml
    //                     cat manifests/dev/deployment.yaml
    //                 '''
    //             }
    //         }
    //     }

    //     stage('Update Deployment File') {
    //         steps {
    //             script {
    //                 def branchName = "${GIT_BRANCH}".split('/').last()
    //                 sh '''
	// 	    	        git config user.email "noc@certdox.io"
    //                 	git config user.name "Jenkins Agent"
    //                 	git add manifests/dev/deployment.yaml
    //                 	git commit -m "Update to version $RELEASE"
    //                 	git push https://$GITHUB_TOKEN@github.com/$GITHUB_USERNAME/$REPO_NAME HEAD:$BRANCH_NAME
	// 	            '''
    //             }
    //         }
    //     }

    }
}