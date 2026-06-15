#!/usr/bin/env bash
set -e
service ssh start
service nginx start
JAR="$(ls /app/target/*.jar | grep -v 'original' | head -n1)"
echo "Starting Spring Boot: ${JAR}"
exec java -jar "${JAR}"
