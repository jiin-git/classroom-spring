package basic.classroom.controller.login;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Member;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.service.InstructorService;
import basic.classroom.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreateMemberController {

    private final InstructorService instructorService;
    private final StudentService studentService;

    @GetMapping("/create/member")
    public String createMemberForm(Model model) {
        model.addAttribute("createMemberForm", new CreateMemberDto());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/createMemberForm";
    }

    @PostMapping("/create/member")
    public String createMember(@Validated @ModelAttribute("createMemberForm") CreateMemberDto createMemberDto, BindingResult bindingResult, Model model) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("student", MemberStatus.STUDENT);
            model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
            return "login/createMemberForm";
        }

        // 성공 로직
        if (createMemberDto.getMemberStatus() == MemberStatus.INSTRUCTOR) {
            Instructor instructor = Instructor.createInstructor(
                    new Member(createMemberDto.getName(), createMemberDto.getLoginId(), createMemberDto.getLoginPw(), createMemberDto.getEmail()));
            instructorService.join(instructor);
        }
        else {
            Student student = Student.createStudent(
                    new Member(createMemberDto.getName(), createMemberDto.getLoginId(), createMemberDto.getLoginPw(), createMemberDto.getEmail()));
            studentService.join(student);
        }

        return "redirect:/login";
    }
}
