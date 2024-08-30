package org.example.catch_line.scrap.service;

import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.scrap.model.key.ScrapId;
import org.example.catch_line.scrap.repository.ScrapRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScrapServiceTest {

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private ScrapRepository scrapRepository;

    @InjectMocks
    private ScrapService scrapService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRestaurantScraps() {
        Long restaurantId = 1L;
        RestaurantEntity restaurant = mock(RestaurantEntity.class);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurant);
        when(scrapRepository.countByRestaurant(restaurant)).thenReturn(5L);

        Long scrapCount = scrapService.getRestaurantScraps(restaurantId);

        assertEquals(5L, scrapCount);
        verify(restaurantValidator).checkIfRestaurantPresent(restaurantId);
        verify(scrapRepository).countByRestaurant(restaurant);
    }

    @Test
    void testHasMemberScrapRestaurant() {
        Long memberId = 1L;
        Long restaurantId = 2L;
        ScrapId scrapId = new ScrapId(memberId, restaurantId);

        when(scrapRepository.existsById(scrapId)).thenReturn(true);

        boolean result = scrapService.hasMemberScrapRestaurant(memberId, restaurantId);

        assertTrue(result);
        verify(scrapRepository).existsById(scrapId);
    }

    @Test
    void testCreateScrap() {
        Long memberId = 1L;
        Long restaurantId = 2L;
        MemberEntity member = mock(MemberEntity.class);
        RestaurantEntity restaurant = mock(RestaurantEntity.class);
        ScrapId scrapId = new ScrapId(memberId, restaurantId);

        when(memberValidator.checkIfMemberPresent(memberId)).thenReturn(member);
        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurant);
        when(scrapRepository.existsById(scrapId)).thenReturn(false);
        when(restaurantMapper.entityToResponse(restaurant, true)).thenReturn(mock(RestaurantResponse.class));

        RestaurantResponse response = scrapService.createScrap(memberId, restaurantId);

        assertEquals(response, response);
        verify(scrapRepository).save(any(ScrapEntity.class));
        verify(restaurant).addScrapCountByUser();
    }

    @Test
    void testDeleteScrap() {
        Long memberId = 1L;
        Long restaurantId = 2L;
        RestaurantEntity restaurant = mock(RestaurantEntity.class);
        ScrapId scrapId = new ScrapId(memberId, restaurantId);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurant);
        doNothing().when(scrapRepository).deleteById(scrapId);
        when(restaurantMapper.entityToResponse(restaurant)).thenReturn(mock(RestaurantResponse.class));

        RestaurantResponse response = scrapService.deleteScrap(memberId, restaurantId);

        assertEquals(response, response);
        verify(scrapRepository).deleteById(scrapId);
        verify(restaurant).reduceScrapCountByUser();
    }
}