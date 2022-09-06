# RTU-rent-server
ren2u spring boot server

API SEVER URL:    
ec2-15-165-38-225.ap-northeast-2.compute.amazonaws.com
- 눌러서 들어갔을 때 "Server is Running :)" 이라고 뜨지 않으면 서버가 다운되어 있는 것. 이해찬에게 DM

API SPECIFICATION:   
https://documenter.getpostman.com/view/20800581/UzR1M3qi  
이 문서에 API 상세 설명 적어놓았습니다.

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

### ec2
```
git pull
ps aux | grep java
kill -9 [previous pid]
./gradlew build --exclude-task test 
java -jar build/libs/application.war 1>app.log 2>&1 &
disown [pid]
```
### Docker
```
# test실패할 시 build가 안됨. --exclude 옵션은 선택사항
./gradlew build --exclude-task test
# tag는 배포 단위마다 0.0.1씩 증가 시키려고 합니다. develop branch에도 같은 태그를 달아서 docker image와 맞춰주세요!
docker build -t ren2u-spring-boot-docker:1.1.3 .
```

테스트 제외 빌드
```
./gradlew  build --exclude-task test && java -jar build/libs/rentserver-0.0.1-SNAPSHOT.jar
```

그레이들 버전낮아서(4점대?) 7점대로 업글
```
./gradlew wrapper --gradle-version 7.4
```

```
./gradlew clean build #기존 파일을 지우고 새로 빌드
```

refference
- [ec2 spring boot env setting](https://velog.io/@nefertiri/AWS-EC2%EB%A1%9C-%EC%84%9C%EB%B2%84-%EB%A7%8C%EB%93%A4%EA%B8%B0)
- [exception handling](https://bcp0109.tistory.com/303)

