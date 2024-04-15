package basic.classroom.controller.api.members.instructor;

import basic.classroom.domain.Instructor;
import basic.classroom.dto.MyPageResponse;
import basic.classroom.dto.UpdateMyPageRequest;
import basic.classroom.dto.UpdatePasswordRequest;
import basic.classroom.service.members.MemberJpaServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members/instructor/my-page")
public class InstructorMyPageApi {
    private final MemberJpaServiceV2 memberService;

    @GetMapping("")
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        MyPageResponse myPageResponse = MyPageResponse.fromInstructor(instructor);
        return ResponseEntity.ok(myPageResponse);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateMyPage(@AuthenticationPrincipal UserDetails userDetails, @Validated @ModelAttribute UpdateMyPageRequest updateMyPageRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.update(instructor, updateMyPageRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.updatePassword(instructor, updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> initializeProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.initializeProfile(instructor);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
