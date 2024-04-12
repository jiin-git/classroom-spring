package basic.classroom.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "home";
        }

        List<? extends GrantedAuthority> authorities = userDetails.getAuthorities().stream().toList();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("STUDENT"))) {
            return "redirect:/student/lectures";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("INSTRUCTOR"))) {
            return "redirect:/instructor/lectures";
        }

        return "home";
    }

    @GetMapping("/ending/credit")
    public String endingCredit() {
        return "endingCredit";
    }
}
