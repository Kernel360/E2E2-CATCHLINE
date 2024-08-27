package org.example.catch_line.scrap.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.common.model.entity.BaseTimeEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "scrap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScrapEntity extends BaseTimeEntity {

    @EmbeddedId
    private ScrapId scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("restaurantId")
    private RestaurantEntity restaurant;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ScrapEntity that = (ScrapEntity) o;
        return Objects.nonNull(getScrapId()) && Objects.equals(getScrapId(), that.getScrapId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(scrapId);
    }
}
