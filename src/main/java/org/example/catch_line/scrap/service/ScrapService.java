package org.example.catch_line.scrap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.scrap.repository.ScrapRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final MemberValidator memberValidator;
    private final RestaurantValidator restaurantValidator;
    private final ScrapRepository scrapRepository;

    public Long getRestaurantScraps(Long restaurantId) {
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        return scrapRepository.countByRestaurant(restaurant);
    }

    public RestaurantResponse saveScrap(Long memberId, Long restaurantId) {
        // 회원 존재 여부 확인
        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
        // 식당 존재 여부 확인
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

        ScrapId scrapId = new ScrapId(memberId, restaurantId);
        boolean existScrap = scrapRepository.existsById(scrapId);

        // scrap 중복 확인 -> 만약 존재하면 에러 발생하지 않고 ** 무시 **
        if (existScrap) {
            return RestaurantMapper.entityToResponse(restaurant);
        }

        ScrapEntity scrapEntity = createScrapEntity(scrapId, member, restaurant);
        scrapRepository.save(scrapEntity);

        restaurant.addScrapCountByUser();

        return RestaurantMapper.entityToResponse(restaurant);
    }

    public RestaurantResponse deleteScrap(Long memberId, Long restaurantId) {
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

        ScrapId scrapId = new ScrapId(memberId, restaurantId);
        boolean existScrap = scrapRepository.existsById(scrapId);

        if(existScrap) {
            return RestaurantMapper.entityToResponse(restaurant);
        }

        // 식당 스크랩 수 1 감소
        scrapRepository.deleteById(scrapId);
        restaurant.reduceScrapCountByUser();

        return RestaurantMapper.entityToResponse(restaurant);
    }

    private ScrapEntity createScrapEntity(ScrapId scrapId, MemberEntity member, RestaurantEntity restaurant) {
        return ScrapEntity.builder()
                .scrapId(scrapId)
                .member(member)
                .restaurant(restaurant)
                .build();
    }
}

