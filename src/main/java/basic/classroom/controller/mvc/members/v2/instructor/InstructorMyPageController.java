package basic.classroom.controller.mvc.members.v2.instructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/instructor")
public class InstructorMyPageController {
    @GetMapping("/my-page")
    public String myPage() {
        return "member/instructor/v2/myPage";
    }

    @GetMapping("/update/my-page")
    public String updateMyPageForm() {
        return "member/instructor/v2/updateMyPage";
    }

    @GetMapping("/update/my-page/password")
    public String updatePasswordForm(Model model) {
        return "member/instructor/v2/updatePassword";
    }
}
