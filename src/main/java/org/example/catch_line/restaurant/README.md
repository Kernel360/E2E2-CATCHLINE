### 🍽️ 식당 스크랩 구현

#### 📌 식당 기능

- [ ] **스크랩 컨트롤러 (`RestaurantScrapController`)**

    <br>

    - 📌 **식당 스크랩 / 스크랩 취소** : `/restaurants/{restaurantId}/scraps`

    <br>
     
    - 📌 **스크랩수 조회** : `/restaurants/{restaurantId}`

<br>

- [ ] **스크랩 서비스 (`RestaurantScrapService`)**

    <br>

    - ✅ 식당을 사용자의 **스크랩** 리스트에 **저장**하는 기능 `saveRestaurantScrap()`
    
        <br>
  
        - ✅ 스크랩 중복 확인 -> 서비스에서 바로 검증
        - ✅ 사용자 존재 여부 확인 `MemberValidator.checkIfMemberPresent()`
        - ✅ 식당 존재 여부 확인 `RestaurantValidator.checkIfRestaurantPresent()`
        - ✅ 식당 스크랩 수 1 증가 `RestaurantEntity`의 `addScrapCountByUser()`

    - ✅ 식당을 사용자의 **스크랩** 리스트에서 **삭제**하는 기능 `deleteRestaurantScrap()`

        <br>
      
    - [ ] 식당 스크랩 수 조회 기능

<br>

#### 👤 사용자 기능

- [ ] **마이스크랩 컨트롤러 (`MyScrapController`)**
    - url: `/my-page/scraps`

    <br>

- [ ] **마이스크랩 서비스 (`MyScrapService`)**
    - 📌 스크랩한 식당을 조회 
    - `findMyRestaurantScrap()`
