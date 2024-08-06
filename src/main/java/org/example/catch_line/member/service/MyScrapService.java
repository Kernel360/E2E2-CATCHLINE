package org.example.catch_line.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.validate.MemberValidator;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyScrapService {

    private final MemberValidator memberValidator;
    private final RestaurantMapper restaurantMapper;

    public List<RestaurantResponse> findMyRestaurants(Long memberId) {

        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);

        return member.getRestaurantScraps().stream()
                .map(restaurantMapper::entityToResponse)
                .collect(Collectors.toList());

    }

}
