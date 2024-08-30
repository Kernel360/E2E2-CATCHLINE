package org.example.catch_line.kakao.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.kakao.model.dto.KakaoCoordinateResponse;
import org.example.catch_line.kakao.service.KakaoAddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final KakaoAddressService kakaoAddressService;

    @GetMapping("/get-coordinate")
    public KakaoCoordinateResponse addressToCoordinate(@RequestParam String query){
        return kakaoAddressService.addressToCoordinate(query);
    }

    @GetMapping("/get-address")
    public KakaoAddressResponse coordinateToAddress(@RequestParam String x, @RequestParam String y){
        return kakaoAddressService.coordinateToAddress(x, y);
    }


}
