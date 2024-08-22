# 🛠️ Member 기능 구현

## 🚀 구현된 기능

1. **로그인, 로그아웃 `AuthController`**
  - ✅ 이메일, 비밀번호로 로그인 (`/login`)
  - ✅ 로그아웃 (`/logout`)
  - ✅ 탈퇴한 회원은 로그인 불가능


2. **회원가입 `AuthController`**
  - ✅ 회원가입 (`/signup`)
  - ✅ 중복 이메일 검증


  - ⚠️ 탈퇴한 회원의 경우, 동일한 이메일로 재가입 가능하도록 처리 필요
  - 해결 방안:
    1. 탈퇴 시 이메일 변경
    2. 복합 고유 제약 조건 설정 (`email`, `isMemberDeleted`)


3. **회원 정보 조회 `MemberController`**
  - `/members`
  - ✅ 존재하는 회원인지 체크 `MemberValidator.checkIfMemberPresent()`
  - ✅ 탈퇴한 회원은 회원 정보 조회 불가능


4. **회원 정보 수정 `MemberController`**
  - `/members`
  - ✅ 존재하는 회원인지 확인 `MemberValidator.checkIfMemberPresent()`
  - ✅ 탈퇴한 회원은 정보 수정 불가능
  - ✅ 중복 이메일 검증(이메일 변경 시에만) `MemberValidator.checkDuplicateEmail()`

    - ✅ 탈퇴한 회원의 이메일은 검증에서 제외

5. **회원 삭제 `MemberController`**
  - `/members`
  - ✅ 존재하는 회원인지 확인 `MemberValidator.checkIfMemberPresent()`
  - ✅ 탈퇴한 회원은 삭제 불가능 `MemberValidator.checkDuplicateEmail()`

6. **작성한 리뷰 관리 `MyReviewController`**
  - ⬜️ 작성한 리뷰 조회 (`/my-page/reviews`)
  - ⬜️ 작성한 리뷰 삭제 (`/my-page/reviews`)
  - ⬜️ 작성한 리뷰 수정 (`/my-page/reviews`)

7. **스크랩한 식당 관리 `MyScrapController`**
  - ✅ 스크랩한 식당 조회


<br>

## ✅ 리팩토링 기록

1. **Role Enum 문제 해결**
  - ✅ `Enum` 타입을 dto에서 검증하고 싶을 때 `@NotBlank` 대신 `@NotNull` 사용


2. **DTO의 validation 동작 문제 해결**
  - ✅ `controller`에 `@Valid` 어노테이션 추가

3. **회원 엔티티 리팩토링**
  - ✅ `isDeletedMember` 필드 생성자 초기화로 변경

  - ✅ 회원 수정 메서드명 변경 `updateMemberStatus` -> `doWithDrawal`
  - ✅ 필드명 변경 `memberStatus` -> `isDeletedMember`
  - ✅ 필드 타입 변경 `Boolean` -> `boolean`
  - ✅ 회원 탈퇴 메서드 수정
    
    - 메서드명 변경 `updateMemberStatus`  -> `doWithdrwal`
    - 메서드의 파라미터 삭제, 내부에서 항상 `isDeletedMember` 값을 `true`로 변경

5. **회원 검증 메서드 클래스 분리 `validate\MemberValidator`**
  - ✅ 회원 이메일 중복 체크 `checkDuplicateEmail()`
  - ✅ 회원 존재 여부 체크 `checkIfMemberPresent()`
  - ⬜️ 탈퇴 회원 관리

6. **컨트롤러 수정에 따른 서비스 분리**
  - ✅ 회원 가입/로그인 (`AuthService`)
  - ✅ 회원 조회/수정/삭제 (`MemberService`)
  - ⬜️ 마이페이지 - 리뷰 조회/수정/삭제 (`MyReviewService`)
  - ✅ 마이페이지 - 스크랩한 식당 조회 (`MyScrapService`)


## UI 기능 구현

- ✅ 회원 가입

    - ✅ 이메일 중복 체크 -> `ajax`
    - ✅ 비밀번호, 전화번호 검증
    - ✅ 이메일 중복 체크, 모든 필드 채우기 전까지 회원가입 버튼 비활성화
    - ✅ 필드 검사: dto의 validation 결과는 `binding result`에 담긴다. -> thymeleaf 동작 위해 dto에 일부 validation 추가
    - ✅ `signup` 서비스 코드 실행 시 발생하는 예외는 하단에 경고 메시지 `alert` 출력

- ✅ 로그인

    - ✅ `login` 서비스 코드 실행 시 발생하는 예외는 하단에 경고 메시지 `alert` 출력


- 프로필
    
    - 프로필 조회
    - 프로필 수정
    - 프로필 삭제

- OAuth


    - [ ] 로그인
    - [ ] 로그아웃 (Login Redirect URI 등록)



## 🔧 추후 수정할 사항

- ⬜️ 타임리프로 변경? `flutter`?
- ⬜️ 에러 코드 & 예외 처리
- ⬜️ 테스트 코드 작성 (단위 테스트)
- ⬜ 패스워드 VO 객체 추가 ☞ 꼭 하기 🌟