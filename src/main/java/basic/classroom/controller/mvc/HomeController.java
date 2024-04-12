package basic.classroom.controller.mvc;

import basic.classroom.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "home";
        }

        String loginId = userDetails.getUsername();
        Authentication authentication = jwtTokenProvider.getAuthentication(loginId);
        List<? extends GrantedAuthority> authorities = authentication.getAuthorities().stream().toList();

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
