package basic.classroom.controller.api.members.student;

import basic.classroom.domain.Student;
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
@RequestMapping("/api/members/student/my-page")
public class StudentMyPageApi {
    private final MemberJpaServiceV2 memberService;

    @GetMapping("")
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        MyPageResponse myPageResponse = MyPageResponse.fromStudent(student);
        return ResponseEntity.ok(myPageResponse);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateMyPage(@AuthenticationPrincipal UserDetails userDetails, @Validated @ModelAttribute UpdateMyPageRequest updateMyPageRequest) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.update(student, updateMyPageRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.updatePassword(student, updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> initializeProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.initializeProfile(student);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
