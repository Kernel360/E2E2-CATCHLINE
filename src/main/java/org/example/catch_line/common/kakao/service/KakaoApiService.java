package org.example.catch_line.common.kakao.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoApiService {

    private final RestTemplate restTemplate;

    @Value("${kakao.maps.rest-api.key}")
    private String apiKey;

    private static final String ADDRESS_API_URL = "https://dapi.kakao.com/v2/local/search/address";
    private static final String COORDINATE_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address";

    // 주소 검색 -> 지번 주소, 도로명 주소, 위도 경도 정보 얻기.
    public KakaoCoordinateResponse getCoordinateInfo(String address) {
        String url = UriComponentsBuilder.fromHttpUrl(ADDRESS_API_URL)
                .queryParam("query", address)
                .build()
                .toUriString();

        return setHttp(url, KakaoCoordinateResponse.class);
    }

    // 좌표 검색 -> 지번 주소, 도로명 주소 정보 얻기
    public KakaoAddressResponse getAddressInfo(String x, String y) {
        String url = UriComponentsBuilder.fromHttpUrl(COORDINATE_API_URL)
                .queryParam("x", x)
                .queryParam("y", y)
                .build()
                .toUriString();

        return setHttp(url, KakaoAddressResponse.class);
    }

    private <T> T setHttp(String url, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, clazz);

        return response.getBody();
    }

}
