FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app
ENV TZ=Asia/Seoul

RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN mkdir -p /app/logs

COPY build/libs/*-SNAPSHOT.jar app.jar

RUN echo "#!/bin/sh" > /app/start.sh && \
    echo "exec java -Duser.timezone=Asia/Seoul -jar /app/app.jar --spring.profiles.active=\${SPRING_PROFILES_ACTIVE:-prod} --server.port=9013" >> /app/start.sh && \
    chmod +x /app/start.sh

EXPOSE 9013

ENTRYPOINT ["sh", "/app/start.sh"]