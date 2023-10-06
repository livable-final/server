<img alt="image" src="https://github.com/khsrla9806/livable-server/assets/70641477/0908643b-f0d0-4dac-8b29-a8a6f308aca6">


<div align=center><h1> 🏢 오피스 혁신을 위한 통합 플랫폼, 오피스너</h1></div>

> **개발 기간** : 2023.09.11(월) ~ 2023.10.06(목) <br>
> **배포 주소** : [오피스너](https://officener.vercel.app)<br>
> **백엔드 레포지토리** : [백엔드](https://github.com/livable-final/server) <br>
> **프론트 유저 레포지토리** : [프론트](https://github.com/livable-final/client) <br>


<br><br><br><br>

<div align=center><h2>프로젝트 목적</h2></div>

- 기존 오피스너 서비스는 관리자, 관리 멤버 이외의 일반 유저의 가입과 이용 동기가 부족
- 이용자가 매일 사용해야 할 만한 컨텐츠와 기능의 부재
- "유저 가입자 수"와 "WAU" 상승을 목적으로 시작된 기업 연계 프로젝트

<br><br><br><br>

<div align=center><h2>사용한 기술스택</h2></div>

<p align=center>
  <img src="https://img.shields.io/badge/Java (JDK 11)-C70D2C?style=flat&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/SpringBoot (2.7.15)-6DB33F?style=flat&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/QueryDSL-00529B?style=flat&logo=querydsl&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL (8.0.34)-4479A1?style=flat&logo=mysql&logoColor=white">
</p>

<p align=center>
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=junit5&logoColor=white">
  <img src="https://img.shields.io/badge/jacoco-BD081C?style=flat&logo=jacoco&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white">
</p>

<p align=center>
  <img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat&logo=github actions&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon AWS EC2-41454A?style=flat&logo=amazonaws&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon AWS S3-569A31?style=flat&logo=amazon s3&logoColor=white">
</p>

<p align=center>
  <img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat&logo=IntelliJ IDEA&logoColor=white">
  <img src="https://img.shields.io/badge/notion-000000?style=flat&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/slack-4A154B?style=flat&logo=slack&logoColor=white">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white">
</p>


<br><br><br><br>


<div align=center><h2>백엔드 아키텍처</h2></div>

![image](https://github.com/khsrla9806/livable-server/assets/70641477/383dee4a-7032-4ef0-b147-d315a4bb5672)


<br><br><br><br>


<div align=center><h2>ERD</h2></div>

![image](https://github.com/khsrla9806/livable-server/assets/70641477/4ae505f2-139f-406c-b358-b1f11d1982f6)


<br><br><br><br>


<div align=center><h2>Jacoco 테스트 커버리지</h2></div>

> 백엔드팀 테스트 커버리지 목표 40% 이상 달성

![image](https://github.com/khsrla9806/livable-server/assets/70641477/cf7ea67a-e881-4a0a-a26c-4db876acb0ea)



<br><br><br><br>


<div align=center><h2>프로젝트 실행하기</h2></div>

### application.yml
``` yaml
# Spring, DB propertiesg setting
spring:
  datasource:
    url: #DB Address
    driver-class-name: #DB Driver
    username: #DB Username
    password: #DB Password

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        default_batch_fetch_size: 100

# S3 propertiesg setting
cloud:
  aws:
    s3:
      bucket: #S3 bucketName
    credentials:
      accessKey: #S3 accessKey
      secretKey: #S3 secretKey
    region:
      static: ap-northeast-2
    stack:
      auto: false

# JWT properties setting
jwt:
  secret: #JWT key
```

### build and test
```bash
$ ./gradlew clean build
```

### run
```bash
$ java -jar ./build/libs/server-0.0.1-SNAPSHOT.jar
```


<br><br><br><br>


<div align=center><h2>백엔드 팀원</h2></div>
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/hyunsb">
        <img src="https://avatars.githubusercontent.com/u/96504592?v=4" width="200px;" alt=""/><br /><sub><b>정현수</b></sub></a><br />
      </td>
      <td align="center"><a href="https://github.com/khsrla9806">
        <img src="https://avatars.githubusercontent.com/u/70641477?v=4" width="200px;" alt=""/><br /><sub><b>김훈섭</b></sub></a><br />
      </td>
      <td align="center"><a href="https://github.com/cxxxtxxyxx">
        <img src="https://avatars.githubusercontent.com/u/109710879?v=4" width="200px;" alt=""/><br /><sub><b>최태윤</b></sub></a><br />
      </td>
      <td align="center"><a href="https://github.com/james-taeil">
        <img src="https://avatars.githubusercontent.com/u/71359732?v=4" width="200px;" alt=""/><br /><sub><b>김태일</b></sub></a><br />
      </td>
      <td align="center"><a href="https://github.com/jy-b">
        <img src="https://avatars.githubusercontent.com/u/61049995?v=4" width="200px;" alt=""/><br /><sub><b>배종윤</b></sub></a><br />
      </td>
    </tr>
    <tr>
      <td>
        - 데이터베이스 설계<br>
        - API 명세서 설계<br>
        - S3 업로드 환경 구성<br>
        - 포인트 기능 구현<br>
        - 식당 리뷰 기능 구현<br>
        - 다중 이미지 처리 구현<br>
      </td>
      <td>
        - 데이터베이스 설계<br>
        - API 명세서 설계<br>
        - EC2 서버환경 구성<br>
        - 자동화 배포환경 구성<br>
        - SSL 적용 (HTTPS)<br>
        - 초대장 CRUD 구현<br>
        - Kakao 알림톡 적용<br>
      </td>
      <td>
        - 데이터베이스 설계<br>
        - API 명세서 설계<br>
        - 자동화 배포환경 구성<br>
        - 방문증 API 구현<br>
      </td>
      <td>
        - 맡은 역할을 적어주세요.
      </td>
      <td>
        - 맡은 역할을 적어주세요.
      </td>
    </tr>
  </tbody>
</table>
