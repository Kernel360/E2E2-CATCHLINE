package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantScrapService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final RestaurantValidator restaurantValidator;


    public RestaurantResponse saveRestaurantScrap(Long memberId, Long restaurantId) {
        // 회원 존재 여부 확인
        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
        // 식당 존재 여부 확인
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        // scrap 중복 확인 -> 만약 존재하면 에러 발생하지 않고 ** 무시 **
        if (member.getRestaurantScraps().contains(restaurant))
            return RestaurantMapper.entityToResponse(restaurant);

        member.getRestaurantScraps().add(restaurant);

        // 식당 스크랩 수 1 증가
        restaurant.addScrapCountByUser();
        memberRepository.save(member);

        return RestaurantMapper.entityToResponse(restaurant);

    }

    public RestaurantResponse deleteRestaurantScrap(Long memberId, Long restaurantId) {
        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
        RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

        if(!member.getRestaurantScraps().contains(restaurant)) return RestaurantMapper.entityToResponse(restaurant);
        member.getRestaurantScraps().remove(restaurant);

        // 식당 스크랩 수 1 감소
        restaurant.reduceScrapCountByUser();
        memberRepository.save(member);

        return RestaurantMapper.entityToResponse(restaurant);

    }
}
