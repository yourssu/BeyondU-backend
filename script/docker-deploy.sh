#!/bin/bash

# .env 파일 로드
if [ -f .env ]; then
    source .env
else
    echo "Error: .env file not found."
    exit 1
fi

CONTAINER_NAME="${PROJECT_NAME}-container"
IMAGE_TAG="${IMAGE_TAG:-latest}"
IMAGE_NAME="${ECR_REGISTRY}/yourssu/${PROJECT_NAME}:${IMAGE_TAG}"

echo "Starting deployment process for ${PROJECT_NAME}..."
echo "Container name: $CONTAINER_NAME"
echo "Image name: $IMAGE_NAME"

# 1. AWS ECR Public 로그인
echo "Logging in to Amazon ECR Public..."
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws

# 2. 최신 이미지 풀
echo "Pulling the latest image..."
docker pull $IMAGE_NAME

# 3. 기존 컨테이너 중지 및 삭제
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Stopping existing container..."
    docker stop $CONTAINER_NAME
fi

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Removing existing container..."
    docker rm $CONTAINER_NAME
fi

# 4. 오래된 이미지 정리 (현재 사용하는 이미지 외 삭제)
echo "Cleaning up old images..."
docker image prune -a -f --filter "until=24h"

# 5. 새 컨테이너 실행
echo "Starting new container on port $SERVER_PORT..."
docker run -d \
  --name $CONTAINER_NAME \
  --restart unless-stopped \
  -p $SERVER_PORT:8080 \
  -v $(pwd)/logs:/app/logs \
  --env-file .env \
  $IMAGE_NAME

echo "Deployment completed successfully!"
echo "Current status of $CONTAINER_NAME:"
docker ps -f name=$CONTAINER_NAME