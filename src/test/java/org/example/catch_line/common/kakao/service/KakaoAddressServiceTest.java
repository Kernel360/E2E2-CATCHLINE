package org.example.catch_line.common.kakao.service;

import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KakaoAddressServiceTest {

    @Mock
    private KakaoApiService kakaoApiService;

    @InjectMocks
    private KakaoAddressService kakaoAddressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddressToCoordinate() {
        String address = "Seoul";
        KakaoCoordinateResponse mockResponse = mock(KakaoCoordinateResponse.class);

        // Mocking the KakaoApiService method
        when(kakaoApiService.getCoordinateInfo(address)).thenReturn(mockResponse);

        KakaoCoordinateResponse response = kakaoAddressService.addressToCoordinate(address);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(kakaoApiService, times(1)).getCoordinateInfo(address);
    }

    @Test
    void testCoordinateToAddress() {
        String x = "127.027619";
        String y = "37.497942";
        KakaoAddressResponse mockResponse = mock(KakaoAddressResponse.class);

        // Mocking the KakaoApiService method
        when(kakaoApiService.getAddressInfo(x, y)).thenReturn(mockResponse);

        KakaoAddressResponse response = kakaoAddressService.coordinateToAddress(x, y);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(kakaoApiService, times(1)).getAddressInfo(x, y);
    }
}