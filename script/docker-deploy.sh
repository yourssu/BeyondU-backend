#!/bin/bash

# 1. .env 파일 로드
if [ -f .env ]; then
    # 주석 제외하고 환경변수 export
    export $(grep -v '^#' .env | xargs)
else
    echo "Error: .env file not found."
    exit 1
fi

# 변수 설정 (기본값 처리)
CONTAINER_NAME="${PROJECT_NAME}-container"
IMAGE_TAG="${IMAGE_TAG:-latest}"
IMAGE_NAME="${ECR_REGISTRY}/yourssu/${PROJECT_NAME}:${IMAGE_TAG}"

echo "===================================================="
echo "🚀 Starting deployment: ${PROJECT_NAME}"
echo "📍 Image: ${IMAGE_NAME}"
echo "📍 Port Mapping: ${SERVER_PORT}:9013"
echo "===================================================="

# 2. AWS ECR Public 로그인
echo "🔐 Logging in to AWS ECR..."
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws

# 3. 최신 이미지 풀
echo "📥 Pulling image..."
docker pull $IMAGE_NAME

# 4. 기존 컨테이너 정리
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "🛑 Stopping existing container..."
    docker stop $CONTAINER_NAME
fi

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "🗑️ Removing existing container..."
    docker rm $CONTAINER_NAME
fi

# 5. 새 컨테이너 실행 (echo와 docker run 사이를 확실히 분리)
echo "🚀 Starting new container on port $SERVER_PORT..."

docker run -d \
  --name $CONTAINER_NAME \
  --restart unless-stopped \
  -p $SERVER_PORT:9013 \
  -v /home/ubuntu/beyondu-api/logs:/app/logs \
  --env-file .env \
  -e SPRING_PROFILES_ACTIVE=prod \
  $IMAGE_NAME

# 6. 배포 확인 (Health Check 대용)
echo "⏳ Waiting for application to start..."
sleep 5
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "✅ Deployment successful!"
    docker ps -f name=$CONTAINER_NAME
else
    echo "❌ Deployment failed. Check docker logs."
    exit 1
fi

# 7. 불필요한 이미지 정리 (댕글링 이미지 삭제)
echo "🧹 Cleaning up old images..."
docker image prune -f