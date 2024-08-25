package org.example.catch_line.scrap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.scrap.repository.ScrapRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final MemberValidator memberValidator;
    private final RestaurantValidator restaurantValidator;
    private final RestaurantMapper restaurantMapper;
    private final ScrapRepository scrapRepository;

    public Long getRestaurantScraps(Long restaurantId) {
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        return scrapRepository.countByRestaurant(restaurant);
    }

    public boolean hasMemberScrapRestaurant(Long memberId, Long restaurantId) {
        ScrapId scrapId = new ScrapId(memberId, restaurantId);
        return scrapRepository.existsById(scrapId);
    }

    public RestaurantResponse createScrap(Long memberId, Long restaurantId) {
        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        ScrapId scrapId = new ScrapId(memberId, restaurantId);
        boolean existScrap = scrapRepository.existsById(scrapId);

        if (existScrap) {
            return restaurantMapper.entityToResponse(restaurant);
        }

        ScrapEntity scrapEntity = new ScrapEntity(scrapId, member, restaurant);
        scrapRepository.save(scrapEntity);
        restaurant.addScrapCountByUser();

        return restaurantMapper.entityToResponse(restaurant, true);
    }

    public RestaurantResponse deleteScrap(Long memberId, Long restaurantId) {
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        ScrapId scrapId = new ScrapId(memberId, restaurantId);

        scrapRepository.deleteById(scrapId);
        restaurant.reduceScrapCountByUser();

        return restaurantMapper.entityToResponse(restaurant);
    }
}

