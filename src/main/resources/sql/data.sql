insert into member (name, nickname, password, email, phone_number, role, is_member_deleted, created_at, modified_at)
values
    ('홍길동', '홍', 'honggildong1@!', 'abc@gmail.com', '010-1234-1234', 'USER', false, '2022-08-04 00:00:00', '2022-08-04 00:00:00'),
    ('김민준', '민', 'minJJun^#1111!', 'min@gmail.com', '010-4321-4321', 'USER', false, '2023-01-04 00:00:00', '2023-01-04 00:00:00'),
    ('박지훈', '박지성', 'park^+JH345!', 'park@gmail.com', '010-8888-8888', 'USER', false, '2024-03-04 00:00:00', '2024-04-03 12:00:00'),
    ('이지아', '솔라', 'leezi=-a1234!', 'lee@gmail.com', '010-0000-0000', 'USER', false, '2024-04-01 00:00:00', '2024-05-01 12:00:00'),
    ('정지수', '초코', 'chocOJs$oo4!', 'jisoo@gmail.com', '010-4568-3321', 'USER', false, '2024-05-23 00:00:00', '2024-08-03 12:00:00'),
    ('최유진', '민트', 'mint%Yu-22!!', 'yoo@gmail.com', '010-4321-3214', 'OWNER', false, '2024-01-05 00:00:00', '2024-07-03 12:00:00');


insert into restaurant (name, description, phone_number, rating, latitude, longitude, review_count, scrap_count, food_type, service_type, created_at, modified_at)
values
    ('연돈', '골목식당 돈까스 맛집', '01012341234', 4.8, 3.7774, 3.555, 9300, 12000, 'KOREAN', 'RESERVATION', '2019-01-01 00:00:00', '2024-08-03 12:00:00'),
    ('하노이', '봉은사 쌀국수 맛집', '0212341234', 4.1, 3.13322, 1.6122, 200, 400, 'KOREAN', 'WAITING', '2022-03-04 00:00:00', '2024-07-03 12:00:00'),
    ('천미향', '봉은사 중국집', '0299999999', 4.5, 3.33441, 4.43261, 100, 500, 'CHINESE', 'WAITING', '2010-11-01 00:00:00', '2024-04-03 12:00:00'),
    ('이남장', '봉은사 설렁탕 맛집', '0255555555', 4.7, 3.4123, 5.1711, 1200, 3000, 'KOREAN', 'WAITING', '2005-01-02 00:00:00', '2022-05-03 12:00:00'),
    ('시마스시', '코엑스 스시 맛집', '0243211234', 3.5, 3.8494, 4.24334, 50, 10, 'JAPANESE', 'WAITING', '2020-06-08 00:00:00', '2024-08-03 12:00:00'),
    ('매드포갈릭 봉은사', '봉은사 양식 맛집', '0277771234', 3.9, 3.1723, 3.44312, 90, 100, 'WESTERN', 'WAITING', '2021-05-07 00:00:00', '2024-08-03 12:00:00');

insert into menu (name, price, restaurant_id)
values
    ('안심', 13000, 1), ('등심', 12000, 1), ('치즈', 15000, 1), ('카레', 5000, 1), ('음료', 2000, 1),
    ('쌀국수', 10000, 2), ('나시고랭', 11000, 2), ('나시고랭', 11000, 2), ('팟타이', 12000, 2), ('핫윙', 6000, 2), ('음료', 2000, 2),
    ('짜장', 8000, 3), ('짬뽕', 11000, 3), ('볶음밥', 10000, 3), ('탕수육', 25000, 3), ('깐풍기', 35000, 3), ('유린기', 35000, 3), ('음료', 2000, 3),
    ('설렁탕', 13000, 4), ('특 설렁탕', 21000, 4), ('내장탕', 16000, 4), ('도가니탕', 30000, 4), ('수육', 60000, 4), ('음료', 2000, 4),
    ('광어', 4800, 5), ('참치대뱃살', 10000, 5), ('연어', 5500, 5), ('농어', 6500, 5), ('회 비빔밥', 14000, 5), ('냉모밀', 8000, 5), ('음료', 2000, 5),
    ('파스타', 25000, 6), ('피자', 27000, 6), ('라이스', 27000, 6), ('스테이크', 51000, 6), ('음료', 5000, 6);

insert into review (rating, content, member_id, restaurant_id, created_at, modified_at)
values
    (5, '맛있어요', 1, 1, '2024-03-03 16:30:00', '2024-03-03 16:30:00'), (2, '맛없어요', 3, 1, '2024-07-03 16:30:00', '2024-07-03 16:30:00'),
    (4, '그냥 그랬어요', 1, 2, '2024-04-03 16:30:00', '2024-04-03 16:30:00'), (3, '맛있어요', 4, 1, '2024-04-03 16:30:00', '2024-04-03 16:30:00'),
    (5, '진짜 맛있어요', 1, 3, '2024-05-03 16:30:00', '2024-05-05 16:30:00'), (5, '맛있어요', 4, 2, '2024-01-03 16:30:00', '2024-01-03 16:30:00'),
    (4, '맛...', 1, 4, '2022-01-01 16:30:00', '2022-01-01 16:30:00'), (1, '최악이에요', 4, 6, '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (5, '맛있어요!', 1, 5, '2023-08-01 16:30:00', '2024-08-03 16:30:00'), (2, '다시 방문 안할듯', 5, 3, '2023-10-03 16:30:00', '2023-10-03 16:30:00'),
    (5, '최고!', 1, 6, '2023-08-03 16:30:00', '2023-08-03 16:30:00'), (4, '맛있어요', 5, 4, '2023-01-01 16:30:00', '2023-01-01 16:30:00'),
    (3, '평범해요', 2, 3, '2024-08-03 16:30:00', '2024-08-03 16:30:00'), (5, '천상의 맛', 5, 5, '2023-05-03 16:30:00', '2023-05-03 16:30:00'),
    (5, '맛있다!', 2, 4, '2024-08-01 16:30:00', '2024-08-01 16:30:00'), (1, '^^', 5, 6, '2024-08-03 16:30:00', '2024-08-03 16:30:00');

insert into reservation (member_count, status, member_id, restaurant_id, reservation_date, created_at, modified_at)
values
    (1, 'SCHEDULED', 1, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (1, 'SCHEDULED', 2, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (2, 'SCHEDULED', 3, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (3, 'SCHEDULED', 4, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (4, 'SCHEDULED', 5, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (5, 'SCHEDULED', 6, 1, '2024-08-10 10:30:00', '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (1, 'CANCELED', 1, 1, '2024-07-09 10:30:00', '2024-07-03 16:00:00', '2024-07-03 16:10:00'),
    (1, 'COMPLETED', 1, 1, '2024-07-13 10:30:00', '2024-07-04 16:30:00', '2024-07-04 16:30:00');


insert into waiting (member_count, waiting_type, status, member_id, restaurant_id, created_at, modified_at)
values
    (100, 'DINE_IN','SCHEDULED', 1, 2, '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (39, 'DINE_IN','SCHEDULED', 2, 2, '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (13, 'DINE_IN','SCHEDULED', 3, 5, '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (2, 'DINE_IN','SCHEDULED', 4, 3, '2024-08-03 16:30:00', '2024-08-03 16:30:00'),
    (1, 'TAKE_OUT','SCHEDULED', 5, 4, '2024-08-03 16:00:00', '2024-08-03 16:00:00'),
    (1, 'TAKE_OUT','CANCELED', 6, 4, '2024-08-01 16:30:00', '2024-08-01 16:35:00'),
    (22, 'TAKE_OUT','COMPLETED', 1, 5, '2024-08-02 16:30:00', '2024-08-02 17:30:00');

insert into scrap (member_id, restaurant_id)
values
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
    (2, 1), (2, 3), (2, 5), (2, 6),
    (3, 2), (3, 3), (3, 4), (3, 5), (3, 6),
    (4, 1),
    (5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6),
    (6, 1), (6, 2), (6, 3), (6, 4);

insert into restaurant_hour (day_of_week, open_status, open_time, close_time, restaurant_id)
values
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 1), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 1), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 1), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 1), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 1),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 1), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 1),
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 2), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 2), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 2), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 2), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 2),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 2), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 2),
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 3), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 3), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 3), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 3), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 3),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 3), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 3),
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 4), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 4), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 4), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 4), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 4),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 4), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 4),
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 5), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 5), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 5), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 5), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 5),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 5), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 5),
    ('MONDAY', 'OPEN', '09:00:00', '20:00:00', 6), ('TUESDAY', 'OPEN', '09:00:00', '20:00:00', 6), ('WEDNESDAY', 'OPEN', '09:00:00', '20:00:00', 6), ('THURSDAY', 'OPEN', '09:00:00', '20:00:00', 6), ('FRIDAY', 'OPEN', '09:00:00', '20:00:00', 6),
    ('SATURDAY', 'CLOSE', '09:00:00', '09:00:00', 6), ('SUNDAY', 'CLOSE', '09:00:00', '09:00:00', 6);
