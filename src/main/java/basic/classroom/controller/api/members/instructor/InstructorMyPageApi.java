package basic.classroom.controller.api.members.instructor;

import basic.classroom.domain.Instructor;
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
@RequestMapping("/api/members/instructor/my-page")
public class InstructorMyPageApi {
    private final MemberJpaServiceV2 memberService;

    @GetMapping("")
    public ResponseEntity<MyPageResponse> getInstructorMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        MyPageResponse myPageResponse = MyPageResponse.fromInstructor(instructor);
        return ResponseEntity.ok(myPageResponse);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<byte[]> getInstructorProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
//        String loginId = userDetails.getUsername();
//        Instructor instructor = memberService.findInstructorByLoginId(loginId);
//        ProfileImage profileImage = instructor.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();
//
//        return responseImageData(profileImage, cache);
//    }

    @PutMapping("")
    public ResponseEntity<Void> updateInstructorMyPage(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdateMyPageRequest updateMyPageRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.update(instructor, updateMyPageRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updateInstructorPassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.updatePassword(instructor, updatePasswordRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> initializeInstructorProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        memberService.initializeProfile(instructor);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    private void validatePassword(UpdatePasswordRequest updatePasswordRequest) {
//        String password = updatePasswordRequest.getPassword();
//        String checkPassword = updatePasswordRequest.getCheckPassword();
//        if (!password.equals(checkPassword)) {
//            throw new NotAcceptableStatusException("패스워드가 일치하지 않습니다. 패스워드를 다시 확인해주세요.");
//        }
//    }
//    private ResponseEntity<byte[]> responseImageData(ProfileImage profileImage, CacheControl cache) {
//        byte[] imageData = profileImage.getImageData();
//        String dataType = profileImage.getDataType();
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .cacheControl(cache)
//                .contentType(MediaType.valueOf(dataType))
//                .body(imageData);
//    }
}
