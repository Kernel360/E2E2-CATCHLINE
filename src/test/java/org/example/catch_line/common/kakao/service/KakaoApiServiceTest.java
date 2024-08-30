package org.example.catch_line.common.kakao.service;

import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KakaoApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KakaoApiService kakaoApiService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Reflection을 사용하여 apiKey 필드에 값을 설정
        Field apiKeyField = KakaoApiService.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(kakaoApiService, "testApiKey"); // 테스트용 API 키 설정
    }

    @Test
    void testGetCoordinateInfo() {
        String address = "Seoul";
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address")
                .queryParam("query", address)
                .build()
                .toUriString();

        KakaoCoordinateResponse mockResponse = mock(KakaoCoordinateResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK testApiKey"); // 테스트용 API 키로 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Mocking the RestTemplate.exchange method to return a ResponseEntity with the mocked response
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(KakaoCoordinateResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse)); // Return a non-null ResponseEntity

        KakaoCoordinateResponse response = kakaoApiService.getCoordinateInfo(address);

        assertNotNull(response);
        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(KakaoCoordinateResponse.class));
    }

    @Test
    void testGetAddressInfo() {
        String x = "127.027619";
        String y = "37.497942";
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/geo/coord2address")
                .queryParam("x", x)
                .queryParam("y", y)
                .build()
                .toUriString();

        KakaoAddressResponse mockResponse = mock(KakaoAddressResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK testApiKey"); // 테스트용 API 키로 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Mocking the RestTemplate.exchange method to return a ResponseEntity with the mocked response
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(KakaoAddressResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse)); // Return a non-null ResponseEntity

        KakaoAddressResponse response = kakaoApiService.getAddressInfo(x, y);

        assertNotNull(response);
        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(KakaoAddressResponse.class));
    }
}