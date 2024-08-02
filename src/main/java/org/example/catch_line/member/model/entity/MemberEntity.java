package org.example.catch_line.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.waiting.model.entity.WaitingEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Boolean memberStatus = false;

    @OneToMany(mappedBy = "member")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<WaitingEntity> waitings = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "scrap",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id"))
    List<RestaurantEntity> restaurantScraps = new ArrayList<>();


    @Builder
    public MemberEntity(String email, String name, String nickname, String password, String phoneNumber, Role role, Boolean memberStatus) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.memberStatus = memberStatus;
    }
}
