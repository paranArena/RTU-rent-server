# RTU-rent-server
RenToU rent spring server

API SEVER URL:
https://rtu-rent-server-uwdjr.run.goorm.io
- 눌러서 들어갔을 때 "Server is Running :)" 이라고 뜨지 않으면 서버가 다운되어 있는 것. 이해찬에게 말씀해주세요!
- spring 서버와 연결된 database는 걔속 초기화됩니다.(제가 개발하면서 서버를 실행시킬때마다..)

API SPECIFICATION:
https://documenter.getpostman.com/view/20800581/UzR1M3qi
- 원래 됐던 request가 갑자기 error가 나거나 이상하다면 spec을 다시 확인해주세요!
- requset/response 형식이나, url이 변경됐을 수도 있습니다.

# Client-side Test
1. API SPEC에서 request경로와 request 양식, response 예시 확인
2. 테스트하고자하는 API를 위의 SERVER URL + SPEC상의 경로에 request
3. status code 500 -> 서버에러 발생시 이해찬에게 연락
4. 이 외 문의 사항 언제든지 편하게 연락 :)


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

