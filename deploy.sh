#! /bin/bash

PROJECT_PATH=.
PROJECT_BUILD_PATH=build/libs

clear

echo -e "git pull"

git pull

echo -e "\n\n./gradlew build --exclude-task test"

./gradlew build --exclude-task test

CURRENT_PID=$(pgrep -f application.war | head -n 1)

if [ -z "$CURRENT_PID" ]; then
	echo -e "\n\nkeep going"
else
	echo -e "\n\nkill process (pid : $CURRENT_PID)"
	kill -9 $CURRENT_PID
fi

#echo -e "\n set env"
#export GOOGLE_CLIENT_ID="XXXXX"


echo -e "\n\nRun SpringBoot Application"
JAR_PATH=$(ls ./$PROJECT_BUILD_PATH/ | grep .war | head -n 1)
echo "java -jar $PROJECT_PATH/$PROJECT_BUILD_PATH/$JAR_PATH 1>app.log 2>&1 &"
java -jar $PROJECT_PATH/$PROJECT_BUILD_PATH/$JAR_PATH 1>app.log 2>&1 &

CURRENT_PID=$(pgrep -f application.war | head -n 1)
echo -e "\n\ndisown $CURRENT_PID"
disown $CURRENT_PID
