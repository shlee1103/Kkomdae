pipeline {
    agent any

    /////////////////////////////////////////////////////////////////////////
    // 환경 변수 설정
    /////////////////////////////////////////////////////////////////////////
    environment {
        DOCKER_IMAGE        = "dororo737/d-101"        // Docker Hub 리포지토리 이름
        DOCKER_TAG          = "latest"                 // 태그
        REGISTRY_CREDENTIAL = "REGISTRY_CREDENTIAL"
        SSH_CREDENTIALS     = "SSH_CREDENTIALS"
        EC2_USER            = "ubuntu"
        EC2_HOST            = "j12d101.p.ssafy.io"
        DOCKER_COMPOSE_PATH = '/home/ubuntu/d-101'
    }

    //-----------------------------------------------------------------
    // Jenkins가 사전에 설정해둔 tool 매핑 (PATH 구성)
    //-----------------------------------------------------------------
    tools {
        jdk 'JDK17'
        gradle 'Gradle 8.12.1'
    }

    /////////////////////////////////////////////////////////////////////////
    // 파이프라인 Stages
    /////////////////////////////////////////////////////////////////////////
    stages {

        // Checkout 및 백엔드 변경 체크
        stage('Checkout & Diff Check') {
            steps {
                checkout scm
                script {
                    def diffCount = sh(script: "git diff HEAD~1 HEAD --name-only | grep '^backend/' | wc -l", returnStdout: true).trim()
                    env.SKIP_BACKEND_BUILD = (diffCount == '0') ? 'true' : 'false'
                    echo "Backend 변경 건수: ${diffCount} → SKIP_BACKEND_BUILD=${env.SKIP_BACKEND_BUILD}"
                }
            }
        }

        // 백엔드 빌드 및 Docker 이미지 빌드/푸시를 한 스테이지에서 진행
        stage('Build & Docker Build/Push') {
            when { expression { return env.SKIP_BACKEND_BUILD != 'true' } }
            steps {
                // Jenkins Credentials에 등록된 .env 파일을 가져옴
                // credentialsId: 'DB_CONFIG_FILE'는 Jenkins에 등록한 .env 파일 ID
                withCredentials([file(credentialsId: '.env', variable: 'env')]) {
                    // 임시 경로에 저장된 파일을 backend/kkomdae/src/main/resources/.env 위치로 복사
                    sh '''
                        rm -f backend/kkomdae/src/main/resources/.env
                        mkdir -p backend/kkomdae/src/main/resources
                        cp "$env" backend/kkomdae/src/main/resources/.env
                        cat backend/kkomdae/src/main/resources/.env
                    '''
                }
                // backend/kkomdae 디렉토리로 이동하여 빌드 및 Docker 작업 진행
                dir('backend/kkomdae') {
                    // Gradle 빌드
                    sh '''
                    chmod +x gradlew
                    ./gradlew clean build -Dspring.profiles.active=prod
                    '''
                    // JAR 파일을 Dockerfile 경로로 복사
                    sh 'cp build/libs/kkomdae-0.0.1-SNAPSHOT.jar ./app.jar'

                    // Docker 빌드 및 푸시
                    script {
                        docker.withRegistry('https://index.docker.io/v1/', REGISTRY_CREDENTIAL) {
                            def app = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}", "--no-cache .")
                            sh 'docker info'
                            app.push()
                        }
                    }
                }
            }
        }

        // Deploy 단계: 원격 서버에서 docker-compose를 이용해 배포
        stage('Deploy') {
            when { expression { return env.SKIP_BACKEND_BUILD != 'true' } }
            steps {
                echo "[Deploy] Deploying to ${EC2_HOST} as ${EC2_USER}"
                sshagent(credentials: ['SSH_CREDENTIALS']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} /bin/bash <<'EOS'
    cd "${DOCKER_COMPOSE_PATH}"
    docker compose pull
    docker compose up -d --force-recreate
    docker system prune -f
    sleep 5
    docker ps || echo "Container check failed"
EOS
                    """
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // 빌드 후 알림 설정
    /////////////////////////////////////////////////////////////////////////
    post {
        success {
            script {
                def Author_ID    = sh(script: "git log -1 --pretty=%an", returnStdout: true).trim()
                def Author_Email = sh(script: "git log -1 --pretty=%ae", returnStdout: true).trim()
                mattermostSend(
                    color: 'good',
                    message: """\
✅ 빌드 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER}
작성자: ${Author_ID} (${Author_Email})
(<${env.BUILD_URL}|상세보기>)
""",
                    endpoint: 'https://meeting.ssafy.com/hooks/awheadganfnkjqc33r48z8xceo',
                    channel: '[🔔알림] 꼼대[🔔알림] 꼼대'
                )
            }
        }
        failure {
            script {
                def Author_ID    = sh(script: "git log -1 --pretty=%an", returnStdout: true).trim()
                def Author_Email = sh(script: "git log -1 --pretty=%ae", returnStdout: true).trim()
                mattermostSend(
                    color: 'danger',
                    message: """\
❌ 빌드 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER}
작성자: ${Author_ID} (${Author_Email})
(<${env.BUILD_URL}|상세보기>)
""",
                    endpoint: 'https://meeting.ssafy.com/hooks/awheadganfnkjqc33r48z8xceo',
                    channel: '[🔔알림] 꼼대[🔔알림] 꼼대'
                )
            }
        }
    }
}

