# RTU-rent-server
RenToU rent spring server

API SEVER URL:
https://rtu-rent-server-uwdjr.run.goorm.io

# Local Client-side Test
1. develop branch clone
2. 실행 (저는 그냥 인텔리제이로 열어서 실행버튼 눌렀어요..)
3. http://localhost:8080 으로 request Test
  - [Postman API spec](https://documenter.getpostman.com/view/20800581/UzR1M3qi)
  - 1차적으로 위 API spec에 있는 signup, login, getMyInfo 만 리퀘스트 해보시면 될 것같습니다!

문제있으면 카톡으로 말씀해주세요~


---
### goorm server env (이해찬 기록용)
구름 컨테이너 깃허브 템플릿 (유저아이디, 엑세스토큰, 깃레포url)으로 생성

자바 7,8,9 지원.. 우리 프젝은 11 따라서 자바 11버전 설치
```
# apt-get 업데이트
sudo apt-get update && sudo apt-get upgrade

# java 11 설치
sudo apt-get install openjdk-11-jdk

# java 11 적용. 아래 명령어 입력후 11 버전에 해당하는 번호를 입력하고 엔터 키를 누르세요.
sudo update-alternatives --config java
sudo update-alternatives --config javac

# 버전 확인
javac -version
java -version
```
이후 디폴트로 등록


그레이들도 버전낮아서(4점대?) 7점대로 업글
```
./gradlew wrapper --gradle-version 7.4
```
빌드명령어
```
./gradlew build && java -jar ${java.set.build.path}libs/*.jar

# 명령어 순서

./gradlew  build --exclude-task test #로 빌드(자꾸 테스트 오류 떠서)

java -jar build/libs/rentserver-0.0.1-SNAPSHOT.jar
(libs 폴더가 생성되지 않았으면 빌드가 제대로 되지 않은것)

./gradlew clean build #기존 파일을 지우고 새로 빌드
```

