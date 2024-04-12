package basic.classroom.controller.api.login;

import basic.classroom.dto.LoginRequest;
import basic.classroom.service.login.LoginJpaServiceV2;
import basic.classroom.service.login.ResponseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginApi {
    private final LoginJpaServiceV2 loginService;

    @PostMapping("")
    public ResponseEntity<Void> loginMemberV2(@Validated @RequestBody LoginRequest loginRequest) {
        ResponseToken responseToken = loginService.login(loginRequest);
        String jwt = responseToken.getTokenType() + "_" + responseToken.getAccessToken();

        ResponseCookie responseCookie = getResponseCookie(jwt);
        String role = loginRequest.getMemberStatus().toString().toLowerCase();
        HttpHeaders headers = getHttpHeaders(responseCookie, role);

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    private static HttpHeaders getHttpHeaders(ResponseCookie responseCookie, String role) {
        HttpHeaders headers = new HttpHeaders();
        String redirectUrl = "/" + role + "/lectures";
        headers.setLocation(URI.create(redirectUrl));
        headers.set(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return headers;
    }
    private static ResponseCookie getResponseCookie(String jwt) {
        return ResponseCookie.from("Authorization", jwt)
                .httpOnly(true)
                .maxAge(3600)
                .path("/")
                .build();
    }
}
