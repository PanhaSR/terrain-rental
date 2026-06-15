#!/bin/bash
set -e

echo "==> [1/3] Starting SSH on port 22..."
service ssh start

echo "==> [2/3] Starting Spring Boot on port 8080..."
java -jar /app/target/*.jar \
  --server.port=8080 \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}" \
  --spring.jpa.hibernate.ddl-auto=update &

echo "    Waiting for Spring Boot..."
for i in $(seq 1 40); do
  curl -sf http://localhost:8080/api/terrains > /dev/null 2>&1 && echo "    Spring Boot is ready!" && break
  echo "    waiting... ($i/40)"
  sleep 3
done

echo "==> [3/3] Starting NGINX on port 80..."
nginx -g "daemon off;"
