package org.example.catch_line.kakao.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.kakao.model.dto.KakaoCoordinateResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAddressService {

    private final KakaoApiService kakaoApiService;

    public KakaoCoordinateResponse addressToCoordinate(String address){
        return kakaoApiService.getCoordinateInfo(address);
    }

    public KakaoAddressResponse coordinateToAddress(String x, String y){
        return kakaoApiService.getAddressInfo(x, y);
    }
}
