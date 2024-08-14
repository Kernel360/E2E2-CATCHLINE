package org.example.catch_line.user.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.user.member.model.mapper.converter.EmailConverter;
import org.example.catch_line.user.member.model.mapper.converter.PasswordConverter;
import org.example.catch_line.user.member.model.mapper.converter.PhoneNumberConverter;
import org.example.catch_line.user.member.model.vo.Email;
import org.example.catch_line.user.member.model.vo.Password;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false) // 이메일 unique 제약 조건 삭제
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Convert(converter = PasswordConverter.class)
    private Password password;

    @Column(nullable = false)
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    @Column(nullable = false)
    private boolean isMemberDeleted;


    @OneToMany(mappedBy = "member")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<WaitingEntity> waitings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ScrapEntity> scraps = new ArrayList<>();

    @Builder
    public MemberEntity(Email email, String name, String nickname, Password password, PhoneNumber phoneNumber) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
//        this.isMemberDeleted = false;
    }


    // 회원 정보 수정 -> @Setter 사용 대신 메서드를 따로 추가
    public void updateMember(String name, String nickname, Password password, PhoneNumber phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    // 회원 탈퇴 (memberStatus 값만 변경) -> @Setter 사용 대신 메서드를 따로 추가
    public void doWithdrawal() {
        this.isMemberDeleted = true;
    }
}
