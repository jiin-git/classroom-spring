package basic.classroom.controller;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.MemberStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        String memberStatus = String.valueOf(session.getAttribute(SessionConst.MEMBER_STATUS));
        return "redirect:/" + memberStatus.toLowerCase() + "/lectures";
    }
}
