package basic.classroom.service.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseToken {
    private String accessToken; // jwt
    private final String tokenType = "Bearer";

    public ResponseToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
