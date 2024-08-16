package org.example.catch_line.scrap.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<ScrapEntity, ScrapId> {

    boolean existsById(ScrapId scrapId);

    // 특정 유저가 한 스크랩 목록 조회
    List<ScrapEntity> findAllByMember(MemberEntity member);

    // 특정 식당에 대한 스크랩 수를 카운트
    Long countByRestaurant(RestaurantEntity restaurant);
}
