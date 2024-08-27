package org.example.catch_line.scrap.model.key;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScrapId {

    private Long memberId;

    private Long restaurantId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ScrapId scrapId = (ScrapId) o;
        return Objects.nonNull(getMemberId()) && Objects.equals(getMemberId(), scrapId.getMemberId())
                && Objects.nonNull(getRestaurantId()) && Objects.equals(getRestaurantId(), scrapId.getRestaurantId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(memberId, restaurantId);
    }
}
