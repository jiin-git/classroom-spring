package basic.classroom.controller.login.v2;

import basic.classroom.controller.SessionConst;
import basic.classroom.domain.Instructor;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.LoginDto;
import basic.classroom.service.datajpa.LoginJpaService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginJpaService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginDto());
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request, Model model) {

        // 검증 코드
        if (bindingResult.hasErrors()) {
            return goToLoginForm(model);
        }

        if (loginDto.getMemberStatus() == MemberStatus.INSTRUCTOR) {
            Instructor instructor = loginService.instructorLogin(loginDto);
            if (instructor == null) {
                bindingResult.reject("loginFail", "아이디 또는 비밀번호를 다시 확인해주세요.");
                return goToLoginForm(model);
            }

            createSession(request, instructor.getId(), MemberStatus.INSTRUCTOR);
            return "redirect:/instructor/lectures";
        }

        // MemberStatus == STUDENT
        Student student = loginService.studentLogin(loginDto);
        if (student == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 다시 확인해주세요.");
            return goToLoginForm(model);
        }

        createSession(request, student.getId(), MemberStatus.STUDENT);
        return "redirect:/student/lectures";
    }

//    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        expireCookie(request, response);
        return "redirect:/";
    }

    private String goToLoginForm(Model model) {
        model.addAttribute("student", MemberStatus.STUDENT);
        model.addAttribute("instructor", MemberStatus.INSTRUCTOR);

        return "login/loginForm";
    }

    private void createSession(HttpServletRequest request, Long id, MemberStatus memberStatus) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ID, id);
        session.setAttribute(SessionConst.MEMBER_STATUS, memberStatus);
    }

    private void expireCookie(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        if (session != null) {
            session.invalidate();
        }
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
    }
}