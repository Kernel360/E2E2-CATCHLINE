# 🍽️ E2E2-CATCHLINE
- - -

커널360 BE 2기 End to End 2팀 프로젝트입니다 ❕


## 🍎 프로젝트 소개


- 식당을 예약하고, 원격으로 줄을 설 수 있는 플랫폼입니다.
- 사용자는 원하는 지역의 식당을 조회하고, 다양한 기능을 제공받을 수 있습니다. 
- 마음에 드는 식당은 스크랩해서 조회할 수 있습니다.
- 사용자는 방문한 식당에 리뷰를 남길 수 있습니다.

<br>

## 🌟 기획 의도


- 해커톤보다 더 백엔드 아키텍처적으로 깊게 고민해보고 학습해보고자 합니다. 
- 동시성 문제를 경험하고 해결해보려고 합니다. 
- 비슷한 경험을 해볼 수 있는 서비스인 테이블링, 캐치테이블 앱을 참고하기로 했습니다.

<br>

## 👧 팀원 구성


<div align="center">

|                                                                                 **양상원**                                                                                 |                                                                             **이선우**                                                                              |                                                                               **박소은**                                                                               |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| [<img src="https://github.com/user-attachments/assets/7d1b1482-f0cb-466d-94eb-80aa9634ab31" height=160 width=150> <br/> @sangwonsheep](https://github.com/sangwonsheep) | [<img src="https://github.com/user-attachments/assets/e5ef938e-1337-4472-8c72-14b995e7e9f2" height=160 width=150> <br/> @I-migi](https://github.com/I-migi) | [<img src="https://github.com/user-attachments/assets/b2b6a8e1-8eaf-4ae0-a471-b83e2745a862" height=160 width=150> <br/> @soeunnPark](https://github.com/soeunnPark) |

</div>


<br>

## 🍳 기술 스택


### 1. Back-end

    - JAVA 17
    - Spring Boot 3.3.2
    - Spring Data JPA

### 2. Front-end

    - Thymeleaf
    - HTML
    - CSS : bootstrap

### 3. Database

    - MySQL 8


<br>


## ✴️ 채택한 개발 기술과 브랜치 전략


### Thymeleaf, bootstrap

- Thymeleaf
  - rest api로 개발이 가능하며, 웹 기반 언어인 `flutter`와 간단하게 서버 사이드 렌더링으로 구현 가능한 `Thymeleaf` 중에 고민했습니다.
  - 팀원들이 `javascript`를 학습하는데 시간이 많이 소요될 것 같았고, 앱으로 구현 시 세션 로그인 방식을 토큰으로 바로 변경해야 했습니다.
  - `Thymeleaf` 사용 시 프론트엔드보다 백엔드 로직에 집중할 수 있을 것 같아 최종적으로 결정했습니다.

- bootstrap
  - 오픈소스 프론트엔드 프레임워크 선택했습니다.
  - CSS + JavaScript


### 브랜치 전략

- 먼저, 각자 fork 한 리포지토리를 삭제했습니다. 멘토링 과정에서 fork 하지 않고 브랜치 전략만 잘 수립해서 개발하기로 결정했습니다.


- Git-flow 전략을 기반으로 main, develop 브랜치와 feature 보조 브랜치를 운용했습니다. 
- main, develop, feature 브랜치로 나누어 개발을 했습니다.

  - main 브랜치는 배포 단계에서만 사용하는 브랜치입니다. 
  - develop 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.
  - feature 브랜치는 기능 단위로 독립적인 개발 환경을 위하여 사용하고 merge 후 각 브랜치를 삭제해주었습니다. 





<br>


## 📗 프로젝트 구조


```
src
└── main
    └── java
        └── catch_line
            ├── common
            │   ├── constant
            │   ├── controller
            │   ├── model
            │   │   └── session
            ├── config
            ├── exception
            ├── filter
            ├── booking
            │   ├── reservation
            │   └── waiting
            ├── dining
            │   ├── menu
            │   └── restaurant
            ├── history
            ├── notification
            ├── review
            ├── scrap
            ├── statistics
            ├── kakao
            └── user
                ├── auth
                └── member

```

- 도메인 계층 구조를 사용했습니다.
- booking 안에 예약(`reservation`), 웨이팅(`waiting`) 관련 기능들이 존재합니다.
  

<br>


## 👯‍ 역할 분담

 

### 🐑 양상원

- 기능

   - 식당, 리뷰, 메뉴, 영업시간, 스크랩
   - 알림
   - 식당 검색
   - 식당 사장님 기능 (식당 추가, 조회, 수정)

- 통계, 동시성 테스트


### 🍗 이선우

- 기능

   - 웨이팅, 예약, 페이지네이션

- 배포, 로그 패키지, 동시성 테스트


### 🍒 박소은

- 기능

   - 회원가입, 로그인, 회원 프로필, 페이지네이션, 스크랩
   - 식당 사장님 기능(회원가입, 로그인)

- Spring Security, 동시성 테스트


<br>


## ⏰ 개발 기간 및 작업 관리


### 개발 기간

- 전체 개발 기간 : 2024-07-29 ~ 2024-08-30

### 작업 관리

- GitHub Projects와 Issues를 사용하여 진행 상황을 공유했습니다.
- 개발 중 팀원들과 논의할 사항들을 GitHub Discussions에 등록했습니다.
- Github Wiki를 사용하여 개발 중 공부한 내용을 문서로 작성했습니다.
- 매일 스크럼을 통해 개발 방향성에 대한 고민을 나누고 Notion에 회의 내용을 기록했습니다.


<br>



## 🧚 기능 구현


### 1. 🍽️ 식당

#### [식당 상세 정보]
- 로그인하지 않아도 식당 상세 정보에 접근이 가능합니다.
- 식당 제목, 소개글, 식당 평점, 리뷰 수, 영업 시간을 보여줍니다.
- 식당 평점과 리뷰 수는 실제 DB에 있는 리뷰 점수 평균과 식당에 해당하는 리뷰 수입니다.
- 영업 시간은 오늘 날짜를 기준으로 보여줍니다.
- 식당 메뉴와 전체 리뷰에 대한 정보를 해당 페이지에서 조회할 수 있습니다.

#### [메뉴]
- 식당에 대한 전체 메뉴 리스트를 보여줍니다.

#### [리뷰]
- 식당에 대한 전체 리뷰 리스트와 리뷰 평점을 보여줍니다.


### 2. 🏠 메인 화면


#### [식당 프리뷰]

- 메인 화면에서 식당을 리뷰가 많은 순, 평가가 높은 순, 스크랩 많은 순으로 정렬할 수 있습니다.
- 페이지네이션 기능을 추가했습니다.

#### [식당 검색]

- 식당 이름과 음식 종류를 기준으로 식당을 찾을 수 있습니다.
- 음식 종류의 경우 한식, 중식, 일식, 양식 토글로 검색이 가능합니다.


### 3. 🕒 예약 및 웨이팅

#### [예약 및 웨이팅 기능]
- 사용자가 예약 시 등록 시간의 다음 날부터 일주일 후까지만 예약할 수 있으며, 다른 날짜는 선택 불가능합니다.
- 로그인 없이 예약 및 웨이팅 등록 시 예외가 발생합니다.
- 사용자가 본인의 예약과 웨이팅 목록을 조회할 수 있습니다.
- 예약 시, 그 날 몇 번째 예약인지 나타내는 기능을 구현했습니다.
- 유저의 개인 예약 및 웨이팅들을 조회할 수 있습니다.


### 4. 📆 예약 관리 기능


#### [나의 예약 및 웨이팅 내역 조회]

- 예약한 식당과 웨이팅한 식당을 조회할 수 있습니다.
- 방문 예정, 방문 완료, 방문 취소로 구분하여 조회할 수 있습니다.

#### [나의 예약 및 웨이팅 수정 및 취소]

- 방문 예정인 예약을 취소하거나 삭제할 수 있습니다.
- 방문 예정인 웨이팅을 취소할 수 있습니다.



### 5. 🙋‍ 회원 기능

#### [일반 로그인 `DefaultLoginService`]

- 이메일, 비밀번호를 입력하여 로그인합니다.

#### [카카오 로그인 `OAuth2LoginService`]

- 카카오톡 로그인을 사용 가능합니다.

#### [회원가입 `SignUpService`]

- 동일한 이메일로는 가입이 어렵습니다.
- 비밀번호는 검증 이후 암호화하여 DB에 저장됩니다.

#### [로그아웃]

- 쿠키의 JWT 토큰을 삭제하여 로그아웃합니다.

#### [프로필 조회 `MemberProfileService`]

- 사용자 프로필을 조회하고, 스크랩한 식당과 작성한 리뷰를 조회할 수 있습니다.

#### [프로필 수정 `MemberProfileService`]

- 이메일 수정 시 중복 이메일 검사를 다시 수행합니다.

#### [회원 탈퇴 `AuthService`]

- 회원 탈퇴 시, 회원 상태를 탈퇴 상태로 변경합니다.
- DB에서 영구적으로 삭제하지 않습니다.
- 탈퇴 후 같은 이메일로 재가입이 가능하도록 `email` 칼럼의 `unique` 제약 조건을 해제했습니다.

#### [스크랩]

- 사용자는 마음에 드는 식당을 스크랩할 수 있습니다.



### 6. 🚨 알림 기능

- 예약 생성, 수정, 취소, 웨이팅 생성, 취소 시에 브라우저로 알림이 옵니다.
- SSE


<br>


## 🌟 구현 현황


<br>


### 💥 1. 동시성

- 하나의 식당에 여러 사용자가 동시에 예약을 하는 상황을 경험해보기 위해 `java.util.concurrent` 패키지를 사용하였습니다. 
- `CountDownLatch`, `ExecutorService`를 이용하여 동시성 테스트를 진행하였습니다.
- 동시에 200명, 1000명의 사용자가 동시에 하나의 식당에 예약을 했을 때 한 명의 사용자만 예약이 되어야 합니다.
- 처음엔 실패를 반복하고, 트랜잭션 격리 수준을 조절해보며 동시성 문제에 대해 경험을 해보았습니다.
- `synchronized`, `비관적 락`을 통해 동시성 문제를 해결할 수 있었습니다.
- 최종적으로 `비관적 락`을 이용하여 동시성 문제를 해결하였습니다.
- 그 이유는 `synchronized`의 경우 메서드 전체에 걸어야 했기 때문에 다른 스레드 혹은 다른 유저가 대기해야 하는 상황이 발생할 것으로 예상했습니다.
- 그렇기 때문에 `비관적 락`은 조회 시에만 걸어 메서드 전체에 거는 것보다 더 성능이 좋을 것이라 예상했고, 동시성 문제도 완벽히 막을 수 있을 것으로 생각하여 결정하게 되었습니다.
- 추후 동시성 방지를 위해 `Redis`를 도입할 계획입니다.



<br>


### 🫥 2. 로그인 하지 않고 사용할 수 있는 기능과 로그인을 해야만 사용할 수 있는 기능을 구분




#### [로그인 하지 않아도 사용할 수 있는 기능]

- 메인 페이지(식당 리스트 조회) 접근

&emsp; <img src="https://github.com/user-attachments/assets/e03f1161-0ea7-4c33-a843-0b964bc70595" alt="이미지 설명" width="400"/>

<br>


- 식당 리스트 조회

    - 식당 검색 기능
    - 식당 정렬 기능
    - 페이지네이션 기능

&emsp; <img src="https://github.com/user-attachments/assets/984fa93c-83e8-40b4-a3cf-cacf66baa423" alt="식당 리스트 조회 기능 화면" width="400"/>
 

<br>


- 식당 위치, 영업 시간, 메뉴 등을 확인할 수 있는 식당 상세 페이지 조회

&emsp; <img src="https://github.com/user-attachments/assets/6bc5102b-a398-4bad-b57b-e676e4e742d8" width="400"/>


#### [로그인해야 사용할 수 있는 기능]


- 웨이팅/예약 등록

- 회원 프로필 기능

    - 회원 프로필 수정
    - 회원 탈퇴
    - 내 리뷰 조회
    - 내 스크랩 조회

- 리뷰 작성
  
- 스크랩

- 예약 관리 기능

    - 예약 취소 및 수정
    - 웨이팅 취소
    - 예약 및 웨이팅 상태별로 조회(방문 전, 방문 완료, 취소)       



#### 로그인하지 않은 사용자가 인증이 필요한 페이지 접속 시 에러 처리


- 사용자 인증이 필요한 주소 요청이 오면 BasicAuthenticationFilter를 상속 받은 MemberJwtAuthorizationFilter가 동작합니다.
- 토큰이 없는 사용자 요청이 올 경우 에러 메시지와 함께 로그인페이지로 리다이렉트됩니다.


&emsp; <img src="https://github.com/user-attachments/assets/42d62340-cec5-4a60-9a14-1b49634d79ee" alt="이미지 설명" width="400"/>



<br>


### 🟦 사용자 level 구분


- `일반 사용자`와 `식당 사장님`으로 level을 구분했습니다.

    - `일반 사용자`는 식당을 조회하고 마음에 드는 식당을 예약할 수 있습니다.
    - `식당 사장님`은 식당 정보를 추가 및 수정 삭제가 가능하며, `일반 사용자`의 예약 및 웨이팅 내역을 관리할 수 있습니다. 


- 일반 사용자 로그인이 되어 있는 사용자는 /owner 페이지에 접속이 불가능합니다.

    - 일반 사용자와 식당 사장님은 서로의 기능을 사용할 수 없습니다.

&ensp; <img src="https://github.com/user-attachments/assets/3793d615-ab12-4096-8b48-4e572e58baf7" width="400"/>


<br>


### 👱 OAuth + 일반 로그인 (JWT)


OAuth의 경우 카카오 로그인을 사용했습니다.


#### [세션 vs JWT 토큰]

- 세션 방식으로 구현 -> Jwt 토큰 방식으로 변경
- 학습 목적으로 처음에는 세션 방식으로 로그인을 구현했습니다. 멘토링 이후 세션 방식의 단점을 알게 되었고, JWT 토큰 방식으로 변경했습니다.
- 세션 방식의 단점

  - 서버가 한 대일 경우, 문제가 발생할 여지가 적습니다.
  - 서버가 여러 대일 경우, 서버마다 세션 메모리 영역을 따로 가지고 있어 확장성이 낮고 관리가 어렵습니다.

- JWT 토큰의 경우 서버가 secret key만 알고 있다면 어느 서버로 요청이 가도 쉽게 처리할 수 있습니다.


#### [OAuth + 일반 로그인]

- OAuth 로그인 `OAuth2LoginService` (`DefaultOAuth2UserService` 상속)

    - security config 설정을 통해 oauth 로그인 페이지로 요청이 들어오면 실행됩니다.

    ```
    .oauth2Login(login -> login
           .loginPage("/login/oauth")
           .defaultSuccessUrl("/loginSuccess")
           .successHandler(oAuth2SuccessHandler) // OAuth2 성공 핸들러 설정
           .userInfoEndpoint(userInfo->userInfo.userService(oauth2LoginService))
    )
    ```                  


- 일반 로그인 `MemberDefaultLoginService` (`UserDetailsService` 상속)

    - 아래와 같이 authentication manager에 등록되어 실행됩니다.

    ```
    @Bean
    public AuthenticationManager memberAuthenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberDefaultLoginService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }
    ```

    - 일반 로그인 요청의 경우, `MemberJwtAuthenticationFilter`의 `attemptAuthentication`이 실행됩니다.
    
        - `MemberJwtAuthenticationFilter` -> `UsernamePasswordAuthenticationFilter` 상속


- 인증이 필요한 페이지의 경우 `MemberJwtAuthorizationFilter`를 거칩니다. (`BasicAuthenticationFilter` 상속)

    - 쿠키에서 JWT TOKEN을 꺼내 검증을 합니다.
    - 이때 oauth 사용자와 일반 로그인 사용자를 토큰의 username을 기준으로 구분하여 authentication을 설정합니다.


#### [JWT 토큰 저장 방식]


- JWT 토큰은 쿠키에 저장됩니다.
- 로그인 요청 성공 이후 success handler에서 처리합니다.


#### [Member와 Owner]

- 처음 erd를 보시면, 일반 사용자와 식당 사장님은 같은 테이블에 설계되었습니다.
- 구현 중간에 일반 사용자의 연관 관계가 복잡해지면서 식당 사장님을 분리하게 되었습니다.
- 일반 사용자와 식당 사장님의 로그인 페이지도 분리되었고, 다중 로그인 페이지와 관련하여 해결했던 이슈를 아래 WIKI에 정리해뒀습니다.
  https://github.com/Kernel360/E2E2-CATCHLINE/wiki/%EB%8B%A4%EC%A4%91-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%8E%98%EC%9D%B4%EC%A7%80%EC%99%80-authentication-manager-2%EA%B0%9C%EB%A5%BC-%EB%B9%88%EC%9C%BC%EB%A1%9C-%EB%93%B1%EB%A1%9D%ED%95%98%EC%97%AC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95
  
- 일반 사용자 인증이 필요한 페이지에 접속 시 `MemberJwtAuthorizationFilter` 동작

    - `BasicAuthenticationFilter`를 상속 -> authentication manager에 `MemberDefaultLoginService` 등록
    
- 식당 사장님 인증이 필요한 페이지에 접속 시 `OwnerJwtAuthorizationFilter` 동작

    - `BasicAuthenticationFilter`를 상속 -> authentication manager에 `OwnerLoginService` 등록

<br>



### 🗾 카카오 맵

- 카카오 지도 API를 이용하여 식당 정보를 가져왔습니다.
- 다음(Daum) 우편번호 서비스 API를 이용하여 주소 검색을 할 수 있도록 하였습니다.
- 카카오 지도 API를 통해 식당 위치를 보여주고, 식당 주소와 이름에 대한 정보를 가지고 왔습니다.
- 메뉴, 리뷰 등의 부가 정보도 제공해줄 것으로 예상했으나, 제공되지 않아 제공되는 정보만 활용하였습니다.



<br>

### 🔴 Custom Exception

<img src = "https://github.com/user-attachments/assets/f0f23cd4-f6d6-4ee0-8566-5de37451b352" width="400"/>


- RuntimeException을 상속받은 CatchLineException
- 모든 custom exception은 CatchLineException을 상속받습니다. 
- validator 클래스에서 예외를 던지고 있습니다.

    ```java
    public void checkDuplicateEmail(Email email) {
        if(memberRepository.findByEmailAndIsMemberDeletedFalse(email).isPresent())
            throw new DuplicateEmailException(email.toString());
    }
    ```


<br>


### 👨‍💻 로그 패키지





<br>

### 🐑 배포


https://velog.io/@i-migi0104/%EC%8A%A4%ED%94%84%EB%A7%81MySQLRDSEC2-%EB%B0%B0%ED%8F%AC%EA%B3%BC%EC%A0%95



<br>

### 📆 스케줄러





<br>


### 🖼️ 이미지 업로드
- 사장님이 식당을 등록할 때 이미지를 업로드할 수 있습니다.
- 사장님이 등록한 식당 이미지를 식당 상세 조회 화면에서 사용자가 조회할 수 있습니다.
- DB에 이미지 경로를 넣는 방식이 아닌 바이너리 형태로 이미지를 그대로 삽입하고 있습니다.
  - 이 방식을 채택한 이유는 다른 기능 구현에 시간이 더 필요했고, 동시성 테스트에 시간을 더 투자하고자 하였습니다.
  - 이 방식은 데이터가 많아지면 DB 부하가 상당히 커질 수 있기 때문에 경로를 넣는 방식으로 변경할 예정입니다.

<br>


### 🚨 알림

- Polling, Websocket, SSE 방식 중에 알림 기능 구현에 사용할 기술을 고민하였습니다.
- Polling은 주기적으로 요청을 보내기 때문에 실시간성을 필요로 하는 저희에게는 맞지 않다고 판단했습니다.
- Websocket의 경우 양방향 통신이고, SSE의 경우 서버에서 클라이언트로 단뱡향 통신입니다.
- 클라이언트에서 서버로 요청을 보낼 일은 없다고 판단이 되어 SSE를 이용하여 알림 서비스를 구현하였습니다.
- 알림은 예약 생성, 수정, 취소, 웨이팅 생성, 취소 시에 발생하도록 하였습니다.

<br>


### 📊 통계

- 스케줄러를 이용하여 자정마다 전날 예약 인원, 웨이팅 인원 수를 통계 테이블에 기록하였습니다.
- 통계 테이블에는 날짜, 예약 인원 수, 웨이팅 인원 수, 식당 정보가 담겨있습니다.
- 관리자 페이지에서 통계 테이블에 담겨 있는 데이터만 볼 수 있는 통계 화면을 제공합니다.
- `Chart.js`를 이용하여 차트를 그려서 시각적으로 통계를 보여주고자 하였습니다.
- 전체 식당 리스트를 제공하고, 하나의 식당을 선택하면 전체 날짜에 대한 통계를 제공합니다.
  - 이 부분은 최근 1주일 혹은 1개월에 대한 데이터만 보여주는 방식으로 변경할 예정입니다.



## 🍏 추후 구현 사항


### 관리자를 만들어 식당 사장님의 기능을 나누고, 일반 사용자의 level을 추가하려고 합니다.

- 현재 식당 사장님이 식당을 추가, 삭제, 수정할 수 있습니다. 실제 상황에서는 관리자에게 요청이 가면 관리자가 수행해야 하는 작업입니다. 이를 관리자 페이지로 분리해 구현하려고 합니다.
- 일반 사용자의 경우 웨이팅과 예약 횟수 및 결제 금액에 따라 등급을 구분하려고 합니다.




## 기능 명세서

https://www.notion.so/1bf8cb5517d2472ba5707ace267e778b

## API 명세서

https://www.notion.so/API-fd90ef95833046b491d87f67da412cfd
