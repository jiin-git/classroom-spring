package basic.classroom.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_ID) == null) {
            return "home";
        }

        String memberStatus = String.valueOf(session.getAttribute(SessionConst.MEMBER_STATUS)).toLowerCase();
        return "redirect:/" + memberStatus + "/lectures";
    }

    @GetMapping("/ending/credit")
    public String endingCredit() {
        return "endingCredit";
    }
}
