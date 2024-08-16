package org.example.catch_line.token;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
