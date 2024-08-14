package org.example.catch_line.user.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.model.mapper.ReviewMapper;
import org.example.catch_line.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewResponse> getMyReviewList(Long memberId) {
        List<ReviewEntity> reviewList = reviewRepository.findAllByMemberMemberIdOrderByCreatedAtDesc(memberId);

        return reviewList.stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
