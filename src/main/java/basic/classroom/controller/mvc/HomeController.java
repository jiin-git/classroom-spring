package basic.classroom.controller.mvc;

import basic.classroom.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        String token = jwtTokenProvider.getJwt(request);

        if (token == null) {
            return "home";
        }

        String loginId = jwtTokenProvider.getLoginId(token);
        Authentication authentication = jwtTokenProvider.getAuthentication(loginId);
        List<? extends GrantedAuthority> authorities = authentication.getAuthorities().stream().toList();
        String role = null;

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("STUDENT"))) {
            role = "student";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("INSTRUCTOR"))) {
            role = "instructor";
        }

        String redirectLoginHome = role + "/lectures";
        return "redirect:/" + redirectLoginHome;
    }

    @GetMapping("/ending/credit")
    public String endingCredit() {
        return "endingCredit";
    }
}
