package basic.classroom.controller.api.login;

import basic.classroom.dto.FindIds.FindIdsRequest;
import basic.classroom.dto.FindIds.FindIdsResponse;
import basic.classroom.dto.FindPassword.FindPasswordRequest;
import basic.classroom.dto.FindPassword.FindPasswordResponse;
import basic.classroom.service.login.LoginJpaServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login/help")
public class LoginHelpApi {
    private final LoginJpaServiceV2 loginService;

    @GetMapping("/ids")
    public ResponseEntity<FindIdsResponse> findLoginIds(@Validated @ModelAttribute FindIdsRequest findIdsRequest) {
        List<String> loginIds = loginService.findLoginIds(findIdsRequest);
        FindIdsResponse findIdsResponse = FindIdsResponse.builder().status(302).loginIds(loginIds).build();
        return new ResponseEntity<>(findIdsResponse, HttpStatus.FOUND);
    }

    @PutMapping("/password")
    public ResponseEntity<FindPasswordResponse> findLoginPassword(@Validated @RequestBody FindPasswordRequest findPasswordRequest) {
        String randomPassword = loginService.findLoginPassword(findPasswordRequest);
        FindPasswordResponse findPasswordResponse = FindPasswordResponse.builder().status(200).password(randomPassword).build();
        return ResponseEntity.ok(findPasswordResponse);
    }
}
