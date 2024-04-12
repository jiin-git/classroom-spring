//package basic.classroom.controller.mvc.login.v1;
//
//import basic.classroom.domain.MemberStatus;
//import basic.classroom.dto.FindIds;
//import basic.classroom.dto.FindPassword.*;
//import basic.classroom.service.datajpa.LoginHelpJpaService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.List;
//
//@Slf4j
////@Controller
//@RequiredArgsConstructor
//public class LoginHelpController {
//    private final LoginHelpJpaService loginHelpService;
//
////    @GetMapping("/find/ids")
//    public String findLoginIdsForm(Model model) {
//        model.addAttribute("findIdsForm", new FindIds.FindIdsRequest());
//        addMemberStatusToModel(model);
//        return "login/findIdsForm";
//    }
//
////    @PostMapping("/find/ids")
//    public String findLoginIds(@Validated @ModelAttribute("findIdsForm") FindIds.FindIdsRequest findIdsRequest,
//                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            addMemberStatusToModel(model);
//            return "login/findIdsForm";
//        }
//
//        // 성공 로직
//        List<String> loginIds = loginHelpService.findLoginIds(findIdsRequest);
//        redirectAttributes.addFlashAttribute("loginIds", loginIds);
//        return "redirect:/find/ids/result";
//    }
//
////    @GetMapping("/find/ids/result")
//    public String findLoginIdResult(@ModelAttribute("loginIds") List<String> loginIds, Model model) {
//        model.addAttribute("loginIds", loginIds);
//        return "login/findIdsResult";
//    }
//
////    @GetMapping("/find/pw")
//    public String findPwForm(Model model) {
//        model.addAttribute("findPwForm", new FindPasswordRequest());
//        addMemberStatusToModel(model);
//
//        return "login/findPwForm";
//    }
//
////    @PostMapping("/find/pw")
//    public String findLoginPassword(@Validated @ModelAttribute("findPwForm") FindPasswordRequest findPasswordDto,
//                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            addMemberStatusToModel(model);
//            return "login/findPwForm";
//        }
//
//        // 성공 로직
//        String loginPw = loginHelpService.findLoginPassword(findPasswordDto);
//        redirectAttributes.addFlashAttribute("loginPw", loginPw);
//        return "redirect:/find/pw/result";
//    }
//
////    @GetMapping("/find/pw/result")
//    public String findLoginPasswordResult(@ModelAttribute("loginPw") String loginPw, Model model) {
//        model.addAttribute("loginPw", loginPw);
//        return "login/findPwResult";
//    }
//
//    private void addMemberStatusToModel(Model model) {
//        model.addAttribute("student", MemberStatus.STUDENT);
//        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
//    }
//}
