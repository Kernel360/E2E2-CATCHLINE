package org.example.catch_line.concurrency;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.example.catch_line.CatchLineApplication;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CatchLineApplication.class)
@Transactional
@ActiveProfiles("test")
public class ConcurrencyTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private EntityManager entityManager;

    private RestaurantEntity restaurantEntity1;
    private RestaurantEntity restaurantEntity2;
    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;
    private MemberEntity memberEntity4;
    private MemberEntity memberEntity5;
    private MemberEntity memberEntity6;

    WaitingRequest waitingRequest1 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();
    WaitingRequest waitingRequest2 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();
    WaitingRequest waitingRequest3 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();
    WaitingRequest waitingRequest4 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();
    WaitingRequest waitingRequest5 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();
    WaitingRequest waitingRequest6 = WaitingRequest.builder()
            .memberCount(3)
            .waitingType(WaitingType.TAKE_OUT)
            .build();

    ReservationRequest reservationRequest1 = ReservationRequest.builder()
            .memberCount(3)
            .reservationDate(LocalDateTime.of(2024, 8, 22, 15, 0)) // 2024년 8월 22일 오후 3시 00분
            .build();

    ReservationRequest reservationRequest2 = ReservationRequest.builder()
            .memberCount(3)
            .reservationDate(LocalDateTime.of(2024, 8, 22, 15, 0)) // 2024년 8월 22일 오후 3시 00분
            .build();


    @BeforeEach
    void setup() {
        this.restaurantEntity1 = restaurantRepository.findById(1L).orElseThrow();
        this.restaurantEntity2 = restaurantRepository.findById(2L).orElseThrow();
        this.memberEntity1 = memberRepository.findById(1L).orElseThrow();
        this.memberEntity2 = memberRepository.findById(2L).orElseThrow();
        this.memberEntity3 = memberRepository.findById(3L).orElseThrow();
        this.memberEntity4 = memberRepository.findById(4L).orElseThrow();
        this.memberEntity5 = memberRepository.findById(5L).orElseThrow();
        this.memberEntity6 = memberRepository.findById(6L).orElseThrow();
    }

    @Test
    @DisplayName("식당 예약 등록을 2명이상이 했을 때 1명만 되고 1명은 거절이 되게")
    public void testReservationConcurrency() throws InterruptedException {
        int numberOfThreads = 2; // 두 명이 동시에 예약을 시도
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        // 첫 번째 사용자가 예약을 시도하는 작업
        Runnable reservationTask1 = () -> {
            try {
                reservationService.addReserve(restaurantEntity1.getRestaurantId(), reservationRequest1, memberEntity1.getMemberId());
                successfulReservations.incrementAndGet();
            } catch (DuplicateReservationTimeException e) {
                failedReservations.incrementAndGet(); // 예약이 실패했을 경우
            } finally {
                latch.countDown();
            }
        };

        // 두 번째 사용자가 예약을 시도하는 작업
        Runnable reservationTask2 = () -> {
            try {
                reservationService.addReserve(restaurantEntity1.getRestaurantId(), reservationRequest2, memberEntity2.getMemberId());
                successfulReservations.incrementAndGet();
            } catch (DuplicateReservationTimeException e) {
                failedReservations.incrementAndGet(); // 예약이 실패했을 경우
            } finally {
                latch.countDown();
            }
        };

        // 두 명의 사용자가 동시에 예약을 시도하도록 스레드 풀에 작업 제출
        executorService.submit(reservationTask1);
        executorService.submit(reservationTask2);

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(1);

        executorService.shutdown();
    }

    @Test
    @DisplayName("식당 웨이팅 등록을 6명이 동시에 등록")
    public void testWaitingRegistration() throws InterruptedException {
        int numberOfThreads = 6;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            WaitingRequest waitingRequest;
            MemberEntity member;

            switch (i % 6) {
                case 0:
                    waitingRequest = waitingRequest1;
                    member = memberEntity1;
                    break;
                case 1:
                    waitingRequest = waitingRequest2;
                    member = memberEntity2;
                    break;
                case 2:
                    waitingRequest = waitingRequest3;
                    member = memberEntity3;
                    break;
                case 3:
                    waitingRequest = waitingRequest4;
                    member = memberEntity4;
                    break;
                case 4:
                    waitingRequest = waitingRequest5;
                    member = memberEntity5;
                    break;
                case 5:
                    waitingRequest = waitingRequest6;
                    member = memberEntity6;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + (i % 6));
            }

            executorService.submit(() -> {
                try {
                    waitingService.addWaiting(restaurantEntity2.getRestaurantId(), waitingRequest, member.getMemberId());
                    List<WaitingEntity> waitings = waitingRepository.findAll();
                    assertThat(waitings.size()).isEqualTo(numberOfThreads);
                    System.out.println(waitings.size());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        // Clear the Hibernate cache using EntityManager
        entityManager.flush();
        entityManager.clear();

        executorService.shutdown();
    }

}
