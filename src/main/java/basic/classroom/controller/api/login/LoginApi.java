package basic.classroom.controller.api.login;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login/auth/members")
public class LoginApi {
    private final LoginJpaService loginService;

    @PostMapping("/instructor")
    public ResponseEntity<Instructor> loginInstructor(@Validated @ModelAttribute("loginForm") LoginDto loginDto, HttpServletRequest request) {
        Instructor instructor = loginService.instructorLogin(loginDto);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ID, instructor.getId());
        session.setAttribute(SessionConst.MEMBER_STATUS, MemberStatus.INSTRUCTOR);

//        createSession(request, instructor.getId(), MemberStatus.INSTRUCTOR);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/lectures"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(instructor);
    }

    @PostMapping("/student")
    public ResponseEntity<Student> loginStudent(@Validated @ModelAttribute("loginForm") LoginDto loginDto, HttpServletRequest request) {
        Student student = loginService.studentLogin(loginDto);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ID, student.getId());
        session.setAttribute(SessionConst.MEMBER_STATUS, MemberStatus.STUDENT);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/lectures"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(student);
//        createSession(request, student.getId(), MemberStatus.STUDENT);
    }

    @DeleteMapping(value = {"/instructor", "/student"})
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        expireCookie(request, response);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
//        return "redirect:/";
    }
//
//    @DeleteMapping("/student")
//    public ResponseEntity<Void> logoutStudent(HttpServletRequest request, HttpServletResponse response) {
//        expireCookie(request, response);
//        return ResponseEntity.noContent().build();
////        return "redirect:/";
//    }

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
