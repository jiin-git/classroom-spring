package basic.classroom.controller.mvc.login.v2;

import basic.classroom.domain.MemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login/find/password")
public class LoginHelpFindPasswordController {
    @GetMapping("")
    public String findPasswordForm(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/v2/findPasswordForm";
    }

    @GetMapping("/result")
    public String findLoginPasswordResult() {
        return "login/v2/findPasswordResult";
    }
}
