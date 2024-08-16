package org.example.catch_line.review.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.model.entity.BaseTimeEntity;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false,columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @Builder
    public ReviewEntity(Integer rating, String content, MemberEntity member, RestaurantEntity restaurant) {
        this.rating = rating;
        this.content = content;
        this.member = member;
        this.restaurant = restaurant;
    }

    public void updateContent(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
