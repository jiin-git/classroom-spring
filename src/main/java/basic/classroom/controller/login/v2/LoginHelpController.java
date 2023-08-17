package basic.classroom.controller.login.v2;

import basic.classroom.domain.MemberStatus;
import basic.classroom.dto.FindIdDto;
import basic.classroom.dto.FindPwDto;
import basic.classroom.service.datajpa.LoginHelpJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/find")
public class LoginHelpController {
    private final LoginHelpJpaService loginHelpService;

    @GetMapping("/ids")
    public String findLoginIdsForm(Model model) {
        model.addAttribute("findIdsForm", new FindIdDto());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/findIdsForm";
    }

//    @PostMapping("/find/ids")
    public String findLoginIds(@Validated @ModelAttribute("findIdsForm") FindIdDto findIdDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 로직
        if (bindingResult.hasErrors()) {
            addMemberStatusToModel(model);
            return "login/findIdsForm";
        }

        // 성공 로직
        List<String> loginIds = loginHelpService.findLoginIds(findIdDto);
        redirectAttributes.addFlashAttribute("loginIds", loginIds);
        return "redirect:/find/ids/result";
    }

    @GetMapping("/ids/result")
    public String findLoginIdResult(Model model) {
        List<String> loginIds = new ArrayList<>();
        model.addAttribute("loginIds", loginIds);
        return "login/findIdsResult";
    }

    @GetMapping("/pw")
    public String findPwForm(Model model) {
        model.addAttribute("findPwForm", new FindPwDto());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/findPwForm";
    }

//    @PostMapping("/find/pw")
    public String findLoginPw(@Validated @ModelAttribute("findPwForm") FindPwDto findPwDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            addMemberStatusToModel(model);
            return "login/findPwForm";
        }

        // 성공 로직
        String loginPw = loginHelpService.findLoginPw(findPwDto);
        redirectAttributes.addFlashAttribute("loginPw", loginPw);
        return "redirect:/find/pw/result";
    }

    @GetMapping("/pw/result")
    public String findLoginPwResult(Model model) {
        String loginPw = null;
        model.addAttribute("loginPw", loginPw);
        return "login/findPwResult";
    }

    private void addMemberStatusToModel(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
    }
}
