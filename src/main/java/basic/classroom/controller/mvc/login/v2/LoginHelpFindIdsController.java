package basic.classroom.controller.mvc.login.v2;

import basic.classroom.domain.MemberStatus;
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

import static basic.classroom.dto.FindIds.FindIdsRequest;

@Slf4j
@Controller
@RequestMapping("/login/find/ids")
public class LoginHelpFindIdsController {
    @GetMapping("")
    public String findLoginIdsForm(Model model) {
        model.addAttribute("findIdsForm", new FindIdsRequest());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/v2/findIdsForm";
    }

    @PostMapping("")
    public String validateFindLoginIdsForm(@Validated @ModelAttribute("findIdsForm") FindIdsRequest findIdsRequest,
                                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", MemberStatus.STUDENT);
            model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
            return "login/v2/findIdsForm";
        }

        redirectAttributes.addFlashAttribute("findIdsRequest", findIdsRequest);
        return "redirect:/login/find/ids/result";
    }

    @GetMapping("/result")
    public String findLoginIdsResult(@ModelAttribute FindIdsRequest findIdsRequest, Model model) {
        model.addAttribute("findIdsRequest", findIdsRequest);
        return "login/v2/findIdsResult";
    }
}
