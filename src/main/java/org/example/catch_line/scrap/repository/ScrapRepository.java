package org.example.catch_line.scrap.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<ScrapEntity, ScrapId> {

    boolean existsById(ScrapId scrapId);

    List<ScrapEntity> findAllByMemberOrderByCreatedAtDesc(MemberEntity member);

    Long countByRestaurant(RestaurantEntity restaurant);
}
