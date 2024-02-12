#!/bin/bash
# shellcheck disable=SC2164

REPOSITORY=home/ec2-user
PROJECT_NAME=us-server
BUILD_DIRECTORY=build/libs

echo "> 프로젝트 디렉토리 이동"
cd $REPOSITORY/$PROJECT_NAME

echo "> Gradle Build"
bash ./gradlew build -x test

echo "> 빌드 디렉토리 이동"
cd $BUILD_DIRECTORY

BUILD_JAR=$(ls *.jar)
JAR_NAME=$(basename "$BUILD_JAR")
echo "> Build 파일명: $JAR_NAME"

echo "> Build 파일 복사"
cp "$JAR_NAME" /$REPOSITORY/$PROJECT_NAME

CURRENT_PID=$(pgrep -f .jar)
echo "> Running Application PID: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 실행중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> log 디렉토리 생성"
mkdir -p log

echo "> 새 애플리케이션 배포"
nohup java -jar $REPOSITORY/$PROJECT_NAME/"$JAR_NAME" 1>log/log.out 2>log/error.out &