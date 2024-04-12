package basic.classroom.controller.api.members;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import basic.classroom.service.members.MemberJpaServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MembersApi {
    private final MemberJpaServiceV2 memberService;

    @GetMapping("/instructors/{id}")
    public ResponseEntity<Instructor> getInstructor(@PathVariable("id") Long id) {
        Instructor instructor = memberService.findInstructor(id);
        return ResponseEntity.ok(instructor);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
        Student student = memberService.findStudent(id);
        return ResponseEntity.ok(student);
    }
}
