FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

ENV TZ=Asia/Seoul

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN mkdir -p /app/logs

RUN printf '#!/bin/sh\nexec java -Duser.timezone=Asia/Seoul -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}' > /app/start.sh && \
    chmod +x /app/start.sh

COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 9013

ENTRYPOINT ["/bin/bash", "/app/start.sh"]