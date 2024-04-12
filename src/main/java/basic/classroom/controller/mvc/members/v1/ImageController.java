//package basic.classroom.controller.mvc.members.v1;
//
//import basic.classroom.controller.SessionConst;
//import basic.classroom.domain.Instructor;
//import basic.classroom.domain.Lecture;
//import basic.classroom.domain.ProfileImage;
//import basic.classroom.domain.Student;
//import basic.classroom.dto.UpdateMemberDto;
//import basic.classroom.service.datajpa.LectureJpaService;
//import basic.classroom.service.datajpa.MemberJpaService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.CacheControl;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
////@Controller
//@RequiredArgsConstructor
//public class ImageController {
//    private final LectureJpaService lectureService;
//    private final MemberJpaService memberService;
//
////    @GetMapping("/instructor/profile/image")
//    public ResponseEntity<byte[]> instructorProfileImage(HttpSession session) {
//        Instructor instructor = findInstructor(session);
//        ProfileImage profileImage = instructor.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();
//
//        return responseImageData(profileImage, cache);
//    }
//
////    @GetMapping("/student/profile/image")
//    public ResponseEntity<byte[]> studentProfileImage(HttpSession session) {
//        Student student = findStudent(session);
//        ProfileImage profileImage = student.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();
//
//        return responseImageData(profileImage, cache);
//    }
//
////    @GetMapping(value = {"/instructor/lecture/image/{lectureId}", "/student/lecture/image/{lectureId}"})
//    public ResponseEntity<byte[]> lectureImg(@PathVariable Long lectureId) {
//        Lecture lecture = lectureService.findOne(lectureId);
//        ProfileImage profileImage = lecture.getProfileImage();
//        CacheControl cache = CacheControl.noCache().mustRevalidate().sMaxAge(1, TimeUnit.DAYS);
//
//        return responseImageData(profileImage, cache);
//    }
//
////    @PostMapping("/instructor/initialize/profile")
//    public String initializeInstructorProfile(@ModelAttribute("instructor") UpdateMemberDto updateMemberDto, HttpSession session) {
//        Instructor instructor = findInstructor(session);
//        memberService.initializeProfile(instructor);
//        return "redirect:/instructor/update/mypage";
//    }
//
////    @PostMapping("/student/initialize/profile")
//    public String initializeStudentProfile(@ModelAttribute("student") UpdateMemberDto updateMemberDto, HttpSession session) {
//        Student student = findStudent(session);
//        memberService.initializeProfile(student);
//        return "redirect:/student/update/mypage";
//    }
//
//    private Instructor findInstructor(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Instructor instructor = memberService.findInstructor(memberId);
//        return instructor;
//    }
//
//    private Student findStudent(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Student student = memberService.findStudent(memberId);
//        return student;
//    }
//
//    private ResponseEntity<byte[]> responseImageData(ProfileImage profileImage, CacheControl cache) {
//        byte[] imageData = profileImage.getImageData();
//        String dataType = profileImage.getDataType();
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .cacheControl(cache)
//                .contentType(MediaType.valueOf(dataType))
//                .body(imageData);
//    }
//}
