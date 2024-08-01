package org.example.catch_line.review.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String content;
}
