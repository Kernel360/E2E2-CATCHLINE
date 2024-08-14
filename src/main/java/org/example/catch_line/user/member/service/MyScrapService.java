package org.example.catch_line.user.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.repository.ScrapRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyScrapService {

    private final MemberValidator memberValidator;
    private final ScrapRepository scrapRepository;

    public List<RestaurantPreviewResponse> findMyRestaurants(Long memberId) {

        MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
        List<ScrapEntity> scrapList = scrapRepository.findAllByMember(member);

        return scrapList.stream()
                .map(scrap -> RestaurantPreviewMapper.entityToResponse(scrap.getRestaurant()))
                .collect(Collectors.toList());
    }

}
