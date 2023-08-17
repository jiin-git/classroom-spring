package basic.classroom.controller.api.members;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.dto.UpdatePwDto;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MyPageApi {
    private final MemberJpaService memberService;

    @GetMapping("/instructors/{id}/mypage/update-info")
    public ResponseEntity<UpdateMemberDto> getUpdateInstructorDto(@PathVariable Long id) {
        Instructor instructor = memberService.findInstructor(id);
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(instructor);
        return ResponseEntity.ok(updateMemberDto);
    }

    @GetMapping("/students/{id}/mypage/update-info")
    public ResponseEntity<UpdateMemberDto> getUpdateStudentDto(@PathVariable Long id) {
        Student student = memberService.findStudent(id);
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(student);
        return ResponseEntity.ok(updateMemberDto);
    }

    @PatchMapping("/instructors/{id}/mypage")
    public ResponseEntity<Instructor> updateInstructorMyPage(@PathVariable Long id, @Validated @ModelAttribute("instructor") UpdateMemberDto updateParam) {
        Instructor instructor = memberService.findInstructor(id);
        memberService.update(instructor, updateParam);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/mypage"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(instructor);
    }

    @PatchMapping("/students/{id}/mypage")
    public ResponseEntity<Student> updateStudentMyPage(@PathVariable Long id, @Validated @ModelAttribute("student") UpdateMemberDto updateParam) {
        Student student = memberService.findStudent(id);
        memberService.update(student, updateParam);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/mypage"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(student);
    }

    @PatchMapping("/instructors/{id}/pw")
    public ResponseEntity<String> updateInstructorPw(@PathVariable Long id, @Validated @ModelAttribute("pwForm") UpdatePwDto updateParam) {
        Instructor instructor = memberService.findInstructor(id);
        memberService.updatePassword(instructor, updateParam.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/mypage"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(instructor.getMember().getPassword());
    }

    @PatchMapping("/students/{id}/pw")
    public ResponseEntity<String> updateStudentPw(@PathVariable Long id, @Validated @ModelAttribute("pwForm") UpdatePwDto updateParam) {
        Student student = memberService.findStudent(id);
        memberService.updatePassword(student, updateParam.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/mypage"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(student.getMember().getPassword());
    }
}
