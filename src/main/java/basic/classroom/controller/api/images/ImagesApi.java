package basic.classroom.controller.api.images;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.ProfileImage;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.service.datajpa.LectureJpaService;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImagesApi {
    private final LectureJpaService lectureService;
    private final MemberJpaService memberService;

    @GetMapping("/instructors/{id}")
    public ResponseEntity<byte[]> instructorProfileImage(@PathVariable Long id) {
        Instructor instructor = memberService.findInstructor(id);
        ProfileImage profileImage = instructor.getProfileImage();
        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();

        return responseImageData(profileImage, cache);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<byte[]> studentProfileImage(@PathVariable Long id) {
        Student student = memberService.findStudent(id);
        ProfileImage profileImage = student.getProfileImage();
        CacheControl cache = CacheControl.noCache().mustRevalidate().cachePrivate();

        return responseImageData(profileImage, cache);
    }

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<byte[]> lectureImg(@PathVariable Long lectureId) {
        Lecture lecture = lectureService.findOne(lectureId);
        ProfileImage profileImage = lecture.getProfileImage();
        CacheControl cache = CacheControl.noCache().mustRevalidate().sMaxAge(1, TimeUnit.DAYS);

        return responseImageData(profileImage, cache);
    }

    @DeleteMapping("/instructors/{id}")
    public ResponseEntity<Void> initializeInstructorProfile(@PathVariable Long id, @ModelAttribute("instructor") UpdateMemberDto updateMemberDto) {
        Instructor instructor = memberService.findInstructor(id);
        memberService.initializeProfile(instructor);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/update/mypage"));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> initializeStudentProfile(@PathVariable Long id, @ModelAttribute("student") UpdateMemberDto updateMemberDto) {
        Student student = memberService.findStudent(id);
        memberService.initializeProfile(student);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/update/mypage"));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }

    private ResponseEntity<byte[]> responseImageData(ProfileImage profileImage, CacheControl cache) {
        byte[] imageData = profileImage.getImageData();
        String dataType = profileImage.getDataType();

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(cache)
                .contentType(MediaType.valueOf(dataType))
                .body(imageData);
    }
}
