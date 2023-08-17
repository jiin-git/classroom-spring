package basic.classroom.controller.login.v1;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Member;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.CreateMemberDto;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
//@Controller
@RequiredArgsConstructor
public class CreateMemberController {
    private final MemberJpaService memberService;

//    @GetMapping("/create/member")
    public String createMemberForm(Model model) {
        model.addAttribute("createMemberForm", new CreateMemberDto());
        addMemberStatusToModel(model);
        return "login/createMemberForm";
    }

//    @PostMapping("/create/member")
    public String createMember(@Validated @ModelAttribute("createMemberForm") CreateMemberDto createMemberDto, BindingResult bindingResult, Model model) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            addMemberStatusToModel(model);
            return "login/createMemberForm";
        }
        // 성공 로직
        if (createMemberDto.getMemberStatus() == MemberStatus.INSTRUCTOR) {
            Instructor instructor = Instructor.createInstructor(new Member(createMemberDto));
            memberService.join(instructor);
        }
        else {
            Student student = Student.createStudent(new Member(createMemberDto));
            memberService.join(student);
        }

        return "redirect:/create/member/result";
    }

//    @GetMapping("/create/member/result")
    public String createMemberResult() {
        return "login/createMemberResult";
    }

    private void addMemberStatusToModel(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
    }
}
