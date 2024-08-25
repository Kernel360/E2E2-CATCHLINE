package org.example.catch_line.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.CatchLineApplication;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = CatchLineApplication.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스당 하나의 인스턴스만 생성
public class ConcurrencyTest2 {

    @Autowired private ReservationService reservationService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private WaitingService waitingService;
    @Autowired private WaitingRepository waitingRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    RestaurantEntity restaurantEntity1;
    RestaurantEntity restaurantEntity2;
    ReservationRequest reservationRequest1;
    ReservationRequest reservationRequest2;
    WaitingRequest waitingRequest1;

    @BeforeAll
    void setup() {
         reservationRequest1 = ReservationRequest.builder()
                .memberCount(3)
                .reservationDate(LocalDateTime.of(2024, 8, 22, 15, 0)) // 2024년 8월 22일 오후 3시 00분
                .build();
         reservationRequest2 = ReservationRequest.builder()
                .memberCount(3)
                .reservationDate(LocalDateTime.of(2024, 9, 22, 15, 0)) // 2024년 8월 22일 오후 3시 00분
                .build();
         waitingRequest1 = WaitingRequest.builder()
                .memberCount(3)
                .waitingType(WaitingType.TAKE_OUT)
                .build();
         memberSave();
    }

    @Test
    @DisplayName("식당 동시 예약 200명 - List에 1~200 넣고 Shuffle")
    void testRestaurantConcurrency100() throws InterruptedException {
        // given
        int numberOfThreads = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        List<Integer> memberIds = IntStream.rangeClosed(1, 201).boxed().collect(Collectors.toList());
        Collections.shuffle(memberIds);

        // when
        for (int i = 1; i <= numberOfThreads; i++) {
            int randomMemberId = memberIds.get(i);
            executorService.submit(() -> {
                try {
                    reservationService.addReservation((long) randomMemberId, restaurantEntity1.getRestaurantId(), reservationRequest1);
                    successfulReservations.incrementAndGet();
                } catch (DuplicateReservationTimeException e) {
                    failedReservations.incrementAndGet(); // 예약이 실패했을 경우
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // then
        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(199);
    }

    @Test
    @DisplayName("식당 동시 예약 - ThreadLocalRandom")
    void testRestaurantConcurrency_not_run() throws InterruptedException {
        // given
        int numberOfThreads = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        // when
        for (int i = 1; i <= numberOfThreads; i++) {
            executorService.submit(() -> {
                long randomMemberId = ThreadLocalRandom.current().nextLong(1, 201); // 1부터 200 사이의 랜덤 ID 선택
                try {
                    reservationService.addReservation(randomMemberId, restaurantEntity1.getRestaurantId(), reservationRequest2);
                    successfulReservations.incrementAndGet();
                } catch (DuplicateReservationTimeException e) {
                    failedReservations.incrementAndGet(); // 예약이 실패했을 경우
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // then
        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(199);
    }

    @Test
    @DisplayName("식당 웨이팅 등록을 20명이 동시에 했을 때 -> 웨이팅 등록 번호가 1~20까지 잘 등록되는지 확인")
    public void testWaitingRegistration() throws InterruptedException {
        // given
        List<WaitingEntity> beforeWaitings = waitingRepository.findAll();
        int size = beforeWaitings.size();
        int numberOfThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                long randomMemberId = ThreadLocalRandom.current().nextLong(1, 201);
                try {
                    waitingService.addWaiting(restaurantEntity2.getRestaurantId(), waitingRequest1, randomMemberId);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
                } finally {
                    latch.countDown(); // 작업이 완료되면 CountDownLatch의 카운트를 감소시킴
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();
        List<WaitingEntity> afterWaitings = waitingRepository.findAll();

        // then
        assertThat(afterWaitings).hasSize(size + numberOfThreads);
    }

    private void memberSave() {
        this.restaurantEntity1 = restaurantRepository.findById(1L).orElseThrow();
        this.restaurantEntity2 = restaurantRepository.findById(2L).orElseThrow();

        for (int i = 0; i < 200; i++) {
            MemberEntity member = MemberEntity.builder()
                    .email(new Email("abc" + i + "@gmail.com"))
                    .name("홍길동")
                    .nickname("hong")
                    .password(new Password(passwordEncoder.encode("123qwe!@Q")))
                    .phoneNumber(new PhoneNumber("010-1234-1234"))
                    .build();

            memberRepository.save(member);
        }
    }

}
