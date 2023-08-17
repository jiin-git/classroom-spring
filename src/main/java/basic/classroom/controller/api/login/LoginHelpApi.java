package basic.classroom.controller.api.login;

import basic.classroom.dto.FindIdDto;
import basic.classroom.dto.FindPwDto;
import basic.classroom.service.datajpa.LoginHelpJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login/help")
public class LoginHelpApi {
    private final LoginHelpJpaService loginHelpService;

    @GetMapping("/ids")
    public ResponseEntity<List<String>> findLoginIds(@Validated @ModelAttribute("findIdsForm") FindIdDto findIdDto) {
        List<String> loginIds = loginHelpService.findLoginIds(findIdDto);
        return ResponseEntity.ok(loginIds);
    }

    @GetMapping("/pw")
    public ResponseEntity<String> findLoginPw(@Validated @ModelAttribute("findPwForm") FindPwDto findPwDto) {
        String loginPw = loginHelpService.findLoginPw(findPwDto);
        return ResponseEntity.ok(loginPw);
    }
}
