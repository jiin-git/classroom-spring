package basic.classroom.controller.api.members.student;

import basic.classroom.config.JwtTokenProvider;
import basic.classroom.domain.Student;
import basic.classroom.dto.MyPageResponse;
import basic.classroom.dto.UpdateMyPageRequest;
import basic.classroom.dto.UpdatePasswordRequest;
import basic.classroom.service.datajpa.members.MemberJpaServiceV2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members/student/my-page")
public class StudentMyPageApi {
    private final MemberJpaServiceV2 memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("")
    public ResponseEntity<MyPageResponse> getStudentMyPage(HttpServletRequest request) {
        Student student = findStudent(request);
        MyPageResponse myPageResponse = MyPageResponse.fromStudent(student);
        return ResponseEntity.ok(myPageResponse);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<byte[]> getStudentProfileImage(HttpServletRequest request) {
//        Student student = findStudent(request);
//        ProfileImage profileImage = student.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();
//        return responseImageData(profileImage, cache);
//    }

    @PutMapping("")
    public ResponseEntity<Void> updateStudentMyPage(@Validated @RequestBody UpdateMyPageRequest updateMyPageRequest, HttpServletRequest request) {
        Student student = findStudent(request);
        memberService.update(student, updateMyPageRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updateStudentPassword(@Validated @RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        Student student = findStudent(request);
        memberService.updatePassword(student, updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> initializeStudentProfileImage(HttpServletRequest request) {
        Student student = findStudent(request);
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
    private Student findStudent(HttpServletRequest request) {
        String jwtToken = jwtTokenProvider.getJWTToken(request);
        String loginId = jwtTokenProvider.getLoginId(jwtToken);
        Student student = memberService.findStudentByLoginId(loginId);
        return student;
    }
}
