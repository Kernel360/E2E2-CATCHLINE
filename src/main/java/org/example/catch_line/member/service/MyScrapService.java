package org.example.catch_line.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyScrapService {

    private final MemberValidator memberValidator;

    public List<RestaurantResponse> findMyRestaurants(Long memberId) {

        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);

        return member.getRestaurantScraps().stream()
                .map(RestaurantMapper::entityToResponse)
                .collect(Collectors.toList());

    }

}
