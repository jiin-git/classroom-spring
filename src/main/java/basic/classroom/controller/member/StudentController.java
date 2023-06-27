package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final LectureService lectureService;

    @GetMapping("/student/lectures")
    public String myLecture(HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = studentService.findAllLectures(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);

        return "member/student/lectureList";
    }

    @PostMapping("/student/cancel/lecture/{lectureId}")
    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        studentService.cancelLecture(student.getId(), lectureId);

        return "redirect:/student/lectures";
    }

    @GetMapping("/student/find/lectures")
    public String findLectures(HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = lectureService.findAll();

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);

        return "member/student/findLecture";
    }

    @PostMapping("/student/add/lecture/{lectureId}")
    public String addLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        studentService.addLecture(student.getId(), lectureId);

        return "redirect:/student/lectures";
    }

    @GetMapping("/student/mypage")
    public String myPage(HttpSession session, Model model) {
        Student student = findStudent(session);
        model.addAttribute("student", student);

        return "member/student/myPage";
    }

    @GetMapping("/student/update/mypage")
    public String updateMyPageForm(HttpSession session, Model model) {
        Student student = findStudent(session);
        UpdateMemberDto memberDto = new UpdateMemberDto(student);

        model.addAttribute("student", memberDto);

        return "member/student/updateMyPage";
    }

    @PostMapping("/student/update/mypage")
    public String updateMyPage(@Validated @ModelAttribute("student") UpdateMemberDto updateParam,
                               BindingResult bindingResult, HttpSession session) {
        Student student = findStudent(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/student/updateMyPage";
        }

        // 성공 로직
        studentService.update(student.getId(), updateParam);
        return "redirect:/student/mypage";
    }


    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = studentService.findOne(memberId);

        return student;
    }

}
