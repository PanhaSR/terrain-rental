pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        MYSQL_HOST     = 'terrain-mysql'
        MYSQL_USER     = 'root'
        MYSQL_PASSWORD = 'Hello@123'
        MYSQL_DB       = 'sreng_panha-db'
        CC_EMAIL       = 'srengty@gmail.com'
        DOCKER_WEB     = 'terrain-web'
        ANSIBLE_DIR    = '/home/nha/ansible-deploy'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '==> Checking out source code...'
                checkout scm
            }
        }

        stage('Git Pull on Web Container') {
            steps {
                echo '==> Pulling latest code inside web container...'
                sh 'docker exec ${DOCKER_WEB} bash -c "cd /app && git checkout -- . && git clean -fd && git pull origin main"'
            }
        }

        stage('Build - Maven') {
            steps {
                echo '==> Building Spring Boot project...'
                sh 'docker exec ${DOCKER_WEB} bash -c "cd /app && mvn clean install -DskipTests -q && echo Maven build SUCCESS"'
            }
        }

        stage('Build - NPM') {
            steps {
                echo '==> Checking for NPM assets...'
                sh '''
                    docker exec ${DOCKER_WEB} bash -c \
                    "if [ -f /app/package.json ]; then cd /app && npm install && npm run build && echo NPM SUCCESS; else echo No package.json - skipping; fi"
                '''
            }
        }

        stage('Test - H2 In-Memory DB') {
            steps {
                echo '==> Running tests with H2 test database...'
                sh 'docker exec ${DOCKER_WEB} bash -c "cd /app && mvn clean test -Dspring.profiles.active=test"'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Backup Database') {
            steps {
                echo '==> Backing up MySQL database...'
                sh '''
                    docker exec ${DOCKER_WEB} bash -c \
                    "mysqldump -h terrain-mysql -uroot -pHello@123 sreng_panha-db --single-transaction > /app/backup.sql && ls -lh /app/backup.sql"
                '''
                sh 'docker cp ${DOCKER_WEB}:/app/backup.sql ./backup.sql'
                archiveArtifacts artifacts: 'backup.sql', fingerprint: true
            }
        }

        stage('Deploy - Ansible') {
            steps {
                echo '==> Running Ansible playbook...'
                sh 'cd ${ANSIBLE_DIR} && ansible-playbook -i inventory.ini playbook.yml -v'
            }
        }
    }

    post {
        success {
            mail(
                to: "${CC_EMAIL}",
                subject: "[Jenkins] SUCCESS - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build SUCCESS!\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}"
            )
        }
        failure {
            mail(
                to: "${CC_EMAIL}",
                subject: "[Jenkins] FAILED - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build FAILED!\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\nCommit: ${env.GIT_COMMIT}\nAuthor: ${env.GIT_AUTHOR_NAME}\nURL: ${env.BUILD_URL}console"
            )
        }
        always {
            cleanWs()
        }
    }
}
