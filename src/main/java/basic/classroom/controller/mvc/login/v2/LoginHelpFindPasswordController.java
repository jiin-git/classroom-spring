package basic.classroom.controller.mvc.login.v2;

import basic.classroom.domain.MemberStatus;
import basic.classroom.dto.FindPassword.FindPasswordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/login/find/password")
public class LoginHelpFindPasswordController {
    @GetMapping("")
    public String findPasswordForm(Model model) {
        model.addAttribute("findPasswordForm", new FindPasswordRequest());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/v2/findPasswordForm";
    }

    @PostMapping("")
    public String findLoginPassword(@Validated @ModelAttribute("findPasswordForm") FindPasswordRequest findPasswordRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", MemberStatus.STUDENT);
            model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
            return "login/v2/findPasswordForm";
        }

        redirectAttributes.addFlashAttribute("findPasswordRequest", findPasswordRequest);
        return "redirect:/login/find/password/result";
    }

    @GetMapping("/result")
    public String findLoginPasswordResult(@ModelAttribute("findPasswordRequest") FindPasswordRequest findPasswordRequest, Model model) {
        model.addAttribute("findPasswordRequest", findPasswordRequest);
        return "login/v2/findPasswordResult";
    }
}
