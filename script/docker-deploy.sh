#!/bin/bash

# 1. .env 파일 로드
if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
else
    echo "Error: .env file not found."
    exit 1
fi

CONTAINER_NAME="${PROJECT_NAME}-container"
IMAGE_TAG="${IMAGE_TAG:-latest}"
IMAGE_NAME="${ECR_REGISTRY}/yourssu/${PROJECT_NAME}:${IMAGE_TAG}"

echo "Starting deployment process for ${PROJECT_NAME}..."

# 2. AWS ECR Public 로그인
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws

# 3. 최신 이미지 풀
echo "Pulling the latest image: $IMAGE_NAME"
docker pull $IMAGE_NAME

# 4. 기존 컨테이너 중지 및 삭제
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Stopping existing container..."
    docker stop $CONTAINER_NAME
fi

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Removing existing container..."
    docker rm $CONTAINER_NAME
fi

# 5. 새 컨테이너 실행
echo "Starting new container on port $SERVER_PORT..."
docker run -d \
  --name $CONTAINER_NAME \
  --restart unless-stopped \
  -p $SERVER_PORT:8080 \
  -v $(pwd)/logs:/app/logs \
  --env-file .env \
  $IMAGE_NAME

# 6. 불필요한 이미지 정리
docker image prune -f