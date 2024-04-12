package basic.classroom.controller.api.login;

import basic.classroom.dto.CreateMember.CreateMemberRequest;
import basic.classroom.service.members.MemberJpaServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/login/members")
public class CreateMemberApi {
    private final MemberJpaServiceV2 memberService;

    @PostMapping("")
    public ResponseEntity<Void> createMemberV2(@Validated @RequestBody CreateMemberRequest createMemberRequest) {
        memberService.create(createMemberRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login/create/member/result"));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
