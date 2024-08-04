
`Member`와 관련된 기능을 구현하며 고민했던 내용을 정리해놓았습니다.


- [x] 로그인 `LoginController`

   - [x] 탈퇴한 회원은 로그인 불가능

- [x] 회원가입 `SignUpController`
  
  - [x] 중복 이메일 검증
  - [ ] 탈퇴한 회원의 경우, 동일한 이메일로 다시 가입 가능
  
     - ⚠️이메일의 제약 조건(`unique`)로 인해 이메일 중복 체크 로직을 개선해도 동일한 이메일의 사용자는 가입이 불가능
     - 해결 방안 1. 탈퇴 시 이메일 변경 2. 복합 고유 제약 조건 설정(`email`, `isMemberDeleted`)

<br>

- [x] 회원 정보 조회 `MemberController`
  
  - [x] 존재하는 회원인지 체크 `checkIfMemberPresent()`
  
    - [x] 탈퇴한 회원은 조회 불가능
  
- [x] 회원 정보 수정 `MemberController`

  - [x] 존재하는 회원인지 확인 `checkIfMemberPresent()`
  
    - [x] 탈퇴한 회원은 회원 정보 수정 불가능
  - [x] 중복 이메일 검증(이메일 변경 시에만) `checkDuplicateEmail()`
    
    - [x] 탈퇴한 회원들의 이메일은 검증에서 제외 
  
- [x] 회원 삭제 `MemberController`

  - [x] 존재하는 회원인지 확인
  
    - [x] 탈퇴한 회원은 회원 삭제 불가능

<br>

- [ ] 작성한 리뷰 조회 `MyReviewController`
- [ ] 작성한 리뷰 삭제 `MyReviewController`
- [ ] 작성한 리뷰 수정 `MyReviewController`


<br>

- [ ] 스크랩한 식당 조회 `MyScrapController`

<br>

## 추후 수정할 사항

- [ ] 타임리프로 변경
- [ ] 에러 코드 & 예외
- [ ] 테스트 코드 (단위 테스트)

<br>

## 해결된 사항

- [X] Role Enum이 제대로 안들어오고 있음.
  
  - Enum 타입의 경우 @NotBlank 사용 불가
  - @NotNull로 대체

- [x] dto의 validation 동작
  - Request Dto에 붙여놓은 @NotBlank 등의 어노테이션이 작동하려면 Controller의 RequestBody 앞에 @Valid 붙여주어야 한다.

- [x] 중복 이메일 검증
- [x] 회원 엔티티 리팩토링
  
   - `isDeletedMember`의 경우 필드에서 초기화하던 방식 사용
   - 생성자에서 초기화하는 방식으로 변경
- [x] 회원 정보 수정 기능 리팩토링
  - 메서드명 변경 `updateMemberStatus` -> `doWithDrawal`
  - 필드명 변경 `memberStatus` -> `isDeletedMember`
  - 필드 타입 변경 `Boolean` -> `boolean`
  
     - 회원 탈퇴 여부의 경우 null 값을 고려할 필요가 없음.


- [x] 회원 탈퇴 메서드 수정
    - `doWithDrawal` 메서드의 파라미터 삭제
    
      - 해당 메서드의 경우 `isDeletedMember` 필드를 항상 true로 변경해야만 한다. 
      - 만약 `doWithDrawal(boolean isDeletedMember)`로 파라미터를 받아버리면, 서비스 쪽에서 실수할 가능성이 있다. 
      - 예를 들어 `doWithDrawal(false)`로 회원 탈퇴를 취소시킬 수 있다.
      - 굳이 실수할 법한 요소를 남겨두지 않고 삭제한다.
      - 메서드 내부에서 변수의 값을 `true`로 변경한다.

- [ ] 회원 검증 메서드 클래스 분리 `validate\MemberValidator`
 
  - [ ] 회원 이메일 중복 체크 `checkDuplicateEmail()`

    - [ ] 탈퇴 회원 관리
  - [ ] 회원 존재 여부 체크 `checkIfMemberPresent()`
  
    - [ ] 탈퇴 회원 관리

- [ ] 컨트롤러 수정에 따라 서비스 분리
  
   - [x] 회원 가입/로그인 (`\AuthService`)
   - [x] 회원 조회/수정/삭제 (`\MemberService`)
   - [ ] 마이페이지 - 리뷰 조회/수저/삭제 (`\MyReviewService`)
   - [ ] 마이페이지 - 스크랩한 식당 조회 (`\MyScrapService`)