#!/bin/bash
BUILD_PATH=$(ls /home/api/server/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME" >> /home/api/log/deploy.log

echo "> build 파일 복사" >> /home/api/log/deploy.log
DEPLOY_PATH=/home/
cp $BUILD_PATH $DEPLOY_PATH

echo "> us-server-deploy.jar 교체" >> /home/api/log/deploy.log
CP_JAR_PATH=$DEPLOY_PATH$JAR_NAME
APPLICATION_JAR_NAME=us-server-deploy.jar
APPLICATION_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

ln -Tfs $CP_JAR_PATH $APPLICATION_JAR

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/api/log/deploy.log
CURRENT_PID=$(pgrep -f $APPLICATION_JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $APPLICATION_JAR 배포" >> /home/api/log/deploy.log
nohup java -jar $APPLICATION_JAR >> /home/api/log/deploy.log 2> /home/api/log/deploy_err.log &