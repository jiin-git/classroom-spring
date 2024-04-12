package basic.classroom.controller.mvc.login.v2;

import basic.classroom.domain.MemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login/find/ids")
public class LoginHelpFindIdsController {
    @GetMapping("")
    public String findLoginIdsForm(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/v2/findIdsForm";
    }

    @GetMapping("/result")
    public String findLoginIdsResult() {
        return "login/v2/findIdsResult";
    }
}
