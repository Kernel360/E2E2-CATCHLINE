package org.example.catch_line.concurrency;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.catch_line.CatchLineApplication;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
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
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = CatchLineApplication.class)
@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스당 하나의 인스턴스만 생성
public class ConcurrencyTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

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

    @Autowired private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setup() {


        memberSave();
    }

    private void memberSave() {
        this.restaurantEntity1 = restaurantRepository.findById(1L).orElseThrow();
        this.restaurantEntity2 = restaurantRepository.findById(2L).orElseThrow();
        this.memberEntity1 = memberRepository.findById(1L).orElseThrow();
        this.memberEntity2 = memberRepository.findById(2L).orElseThrow();
        this.memberEntity3 = memberRepository.findById(3L).orElseThrow();
        this.memberEntity4 = memberRepository.findById(4L).orElseThrow();
        this.memberEntity5 = memberRepository.findById(5L).orElseThrow();
        this.memberEntity6 = memberRepository.findById(6L).orElseThrow();

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
            .reservationDate(LocalDateTime.of(2024, 9, 22, 15, 0)) // 2024년 8월 22일 오후 3시 00분
            .build();

    ReservationRequest reservationRequest3 = ReservationRequest.builder()
            .memberCount(3)
            .reservationDate(LocalDateTime.of(2024, 9, 23, 15, 0)) // 2024년 8월 22일 오후 3시 00분
            .build();

    @Test
    @DisplayName("식당 동시 예약 100명")
    void testRestaurantConcurrency100() throws InterruptedException {
        int numberOfThreads = 200; // 두 명이 동시에 예약을 시도
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        List<Runnable> runnables = new ArrayList<>();
        for (int i = 1; i <= numberOfThreads; i++) {
            int finalI = i;
            MemberEntity member = memberRepository.findById(Long.valueOf(finalI)).orElseThrow(() -> new IllegalArgumentException("id : " + finalI));
            log.info("final : {}", finalI);

            Runnable reservationTask = () -> {
                try {
                    reservationService.addReservation(member.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest1);
                    successfulReservations.incrementAndGet();
                } catch (DuplicateReservationTimeException e) {
                    failedReservations.incrementAndGet(); // 예약이 실패했을 경우
                } finally {
                    latch.countDown();
                }
            };
            runnables.add(reservationTask);
        }

        log.info("run : {}", runnables.size());

        for (Runnable runnable : runnables) {
            executorService.submit(runnable);
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        log.info("count: {}" ,failedReservations.get());

        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(199);

        executorService.shutdown();

    }

    @Test
    @DisplayName("식당 동시 예약 100명")
    void testRestaurantConcurrency1000() throws InterruptedException {
        int numberOfThreads = 200; // 두 명이 동시에 예약을 시도
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        List<Runnable> runnables = new ArrayList<>();
        for (int i = 1; i <= numberOfThreads; i++) {
            int finalI = i;
            MemberEntity member = memberRepository.findById(Long.valueOf(finalI)).orElseThrow(() -> new IllegalArgumentException("id : " + finalI));

            Runnable reservationTask = () -> {
                try {
                    reservationService.addReservation(member.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest3);
                    successfulReservations.incrementAndGet();
                } catch (DuplicateReservationTimeException e) {
                    failedReservations.incrementAndGet(); // 예약이 실패했을 경우
                } finally {
                    latch.countDown();
                }
            };
            runnables.add(reservationTask);
        }

        for (Runnable runnable : runnables) {
            executorService.submit(runnable);
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        log.info("count: {}" ,failedReservations.get());

        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(199);

        executorService.shutdown();

    }

    @Test
    @DisplayName("식당 예약 등록을 4명이상이 했을 때 1명만 되고 1명은 거절이 되게")
//    @Transactional
    public void testReservationConcurrency() throws InterruptedException {
        int numberOfThreads = 4; // 두 명이 동시에 예약을 시도
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulReservations = new AtomicInteger(); // 성공한 예약의 수를 카운트
        AtomicInteger failedReservations = new AtomicInteger(); // 실패한 예약의 수를 카운트

        // 첫 번째 사용자가 예약을 시도하는 작업
        Runnable reservationTask1 = () -> {
            try {
                reservationService.addReservation(memberEntity1.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest2);
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
                reservationService.addReservation(memberEntity2.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest2);
                successfulReservations.incrementAndGet();
            } catch (DuplicateReservationTimeException e) {
                failedReservations.incrementAndGet(); // 예약이 실패했을 경우
            } finally {
                latch.countDown();
            }
        };

        Runnable reservationTask3 = () -> {
            try {
                reservationService.addReservation(memberEntity3.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest2);
                successfulReservations.incrementAndGet();
            } catch (DuplicateReservationTimeException e) {
                failedReservations.incrementAndGet(); // 예약이 실패했을 경우
            } finally {
                latch.countDown();
            }
        };

        // 두 번째 사용자가 예약을 시도하는 작업
        Runnable reservationTask4 = () -> {
            try {
                reservationService.addReservation(memberEntity4.getMemberId(), restaurantEntity1.getRestaurantId(), reservationRequest2);
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
        executorService.submit(reservationTask3);
        executorService.submit(reservationTask4);

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        // 한 명의 사용자만 성공해야 함을 검증
        assertThat(successfulReservations.get()).isEqualTo(1);
        assertThat(failedReservations.get()).isEqualTo(3);

        executorService.shutdown();
    }

    @Test
    @DisplayName("식당 웨이팅 등록을 6명이 동시에 했을 때 -> 웨이팅 등록 번호가 1~6까지 잘 등록되는지 확인")
    public void testWaitingRegistration() throws InterruptedException {
        List<WaitingEntity> BeforewaitingEntities = waitingRepository.findAll();
        int size = BeforewaitingEntities.size();

        //given
        int numberOfThreads = 6;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
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
                    assertThat(1).isEqualTo(10);

                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
                } finally {
                    latch.countDown(); // 작업이 완료되면 CountDownLatch의 카운트를 감소시킴
                }
            });

        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        List<WaitingEntity> waitings = waitingRepository.findAll();

        assertThat(waitings).hasSize(size + numberOfThreads);

        // executorService를 종료
        executorService.shutdown();

    }

}
