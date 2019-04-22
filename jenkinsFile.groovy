podTemplate(
        label: 'mypod',
        inheritFrom: 'default',
        containers: [
                containerTemplate(
                        name: 'docker',
                        image: 'docker:18.02',
                        ttyEnabled: true,
                        command: 'cat'
                ),
                containerTemplate(
                        name: 'helm',
                        image: 'ibmcom/k8s-helm:v2.6.0',
                        ttyEnabled: true,
                        command: 'cat'
                ),
                containerTemplate(
                        name: 'kubectl',
                        image: 'lachlanevenson/k8s-kubectl:v1.8.0',
                        ttyEnabled: true,
                        command: 'cat'
                )
        ],
        volumes: [
                hostPathVolume(
                        hostPath: '/var/run/docker.sock',
                        mountPath: '/var/run/docker.sock'
                )
        ]
) {
    node('mypod') {
        def commitId

        stage('Extract') {
            checkout scm
            commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            def ws = pwd()
            echo "work space is : ${workspace}"
            //echo "files: ${files}"
            echo "ws: ${ws}"
        }

        def repository
        stage('Docker') {
            container('docker') {
                echo "workspace: ${workspace}"
                def ws = pwd()

                //echo "files: ${files}"
                echo "ws: ${ws}"
                // sh "docker build -t csye7374 ."
                docker.withRegistry('https://564889057429.dkr.ecr.us-east-1.amazonaws.com/csye7374', 'ecr:us-east-1:awskey') {

                    //build image
                    def customImage = docker.build("csye7374")

                    //push image
                    customImage.push("${commitId}")
                }

            }
        }
        stage('Update Kubernetes') {
            container('kubectl') {
                //sh "kubectl rolling-update csye7374 --image-pull-policy Always --image csye7374=564889057429.dkr.ecr.us-east-1.amazonaws.com/csye7374:${commitId}"
                sh "kubectl set image deployment csye7374 csye7374=564889057429.dkr.ecr.us-east-1.amazonaws.com/csye7374:${commitId}"
            }
        }

    }

}