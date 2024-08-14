package org.example.catch_line.scrap.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@Entity
@Table(name = "scrap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScrapEntity {

    @EmbeddedId
    private ScrapId scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("restaurantId")
    private RestaurantEntity restaurant;

}
