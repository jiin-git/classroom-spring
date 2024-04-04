package basic.classroom.controller.api.members.student;

import basic.classroom.domain.Student;
import basic.classroom.dto.MyPageResponse;
import basic.classroom.dto.UpdateMyPageRequest;
import basic.classroom.dto.UpdatePasswordRequest;
import basic.classroom.service.datajpa.members.MemberJpaServiceV2;
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
    public ResponseEntity<MyPageResponse> getStudentMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        MyPageResponse myPageResponse = MyPageResponse.fromStudent(student);
        return ResponseEntity.ok(myPageResponse);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<byte[]> getStudentProfileImage() {
//        String loginId = userDetails.getUsername();
//        Student student = memberService.findStudentByLoginId(loginId);
//        ProfileImage profileImage = student.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();
//        return responseImageData(profileImage, cache);
//    }

    @PutMapping("")
    public ResponseEntity<Void> updateStudentMyPage(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdateMyPageRequest updateMyPageRequest) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.update(student, updateMyPageRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updateStudentPassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.updatePassword(student, updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> initializeStudentProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        memberService.initializeProfile(student);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    private ResponseEntity<byte[]> responseImageData(ProfileImage profileImage, CacheControl cache) {
//        byte[] imageData = profileImage.getImageData();
//        String dataType = profileImage.getDataType();
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .cacheControl(cache)
//                .contentType(MediaType.valueOf(dataType))
//                .body(imageData);
//    }
//    private void validatePassword(UpdatePasswordRequest updateParam) {
//        String password = updateParam.getPassword();
//        String checkPassword = updateParam.getCheckPassword();
//        if (!password.equals(checkPassword)) {
//            throw new NotAcceptableStatusException("패스워드가 일치하지 않습니다. 패스워드를 다시 확인해주세요.");
//        }
//    }
}
