package basic.classroom.controller.mvc.login.v2;

import basic.classroom.domain.MemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login/create/member")
public class CreateMemberV2Controller {
    @GetMapping("")
    public String createMemberForm(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/v2/createMemberForm";
    }

    @GetMapping("/result")
    public String createMemberResult() {
        return "login/v2/createMemberResult";
    }
}