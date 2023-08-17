package basic.classroom.controller.api.login;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Member;
import basic.classroom.domain.Student;
import basic.classroom.dto.CreateMemberDto;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class CreateMemberApi {
    private final MemberJpaService memberService;

    @PostMapping("/members/student")
    public ResponseEntity<Student> createStudent(@Validated @ModelAttribute("createMemberForm") CreateMemberDto createMemberDto) {
        Student student = Student.createStudent(new Member(createMemberDto));
        Long id = memberService.join(student);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/create/member/result"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(memberService.findStudent(id));
    }

    @PostMapping("/members/instructor")
    public ResponseEntity<Instructor> createInstructor(@Validated @ModelAttribute("createMemberForm") CreateMemberDto createMemberDto) {
        Instructor instructor = Instructor.createInstructor(new Member(createMemberDto));
        Long id = memberService.join(instructor);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/create/member/result"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(memberService.findInstructor(id));
    }
}
