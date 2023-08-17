package basic.classroom.controller.members.v2;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
//@RequestMapping("/members")
@RequiredArgsConstructor
public class MyLectureController {

    @GetMapping("/instructor/lectures")
    public String instructorMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        return "member/instructor/lectureList";
    }

    @GetMapping("/student/lectures")
    public String studentMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        return "member/student/lectureList";
    }

    @GetMapping("/instructor/lectures/{lectureId}")
    public String instructorLectureInfo(@PathVariable Long lectureId, Model model) {
        return "member/instructor/lectureInfo";
    }

    @GetMapping("/student/lectures/{lectureId}")
    public String studentLectureInfo(@PathVariable Long lectureId, Model model) {
        return "member/student/lectureInfo";
    }

    @GetMapping("/instructor/lectures/{lectureId}/applicants")
    public String applicantsList(@PathVariable Long lectureId, Model model) {
        return "member/instructor/applicantsInfo";
    }

//    private void pagingMyLectures(Long page, Model model, Student student) {
//        int startPage = 1;
//        int currentPage = page == null ? 1 : page.intValue();
//        int pageSize = 10;
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
//        Page<Lecture> lectures = lectureService.findMyLecturesByPage(student, pageable);
//        List<Integer> showPages = pagingService.getShowPages(pageable, lectures.getTotalPages());
//
//        model.addAttribute("lectures", lectures.getContent());
//        model.addAttribute("pages", showPages);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", lectures.getTotalPages());
//        model.addAttribute("student", student);
//    }
//    private void pagingMyLectures(Long page, Model model, Instructor instructor) {
//        int startPage = 1;
//        int currentPage = page == null ? 1 : page.intValue();
//        int pageSize = 10;
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
//        Page<Lecture> lectures = lectureService.findMyLecturesByPage(instructor, pageable);
//        List<Integer> showPages = pagingService.getShowPages(pageable, lectures.getTotalPages());
//
//        model.addAttribute("lectures", lectures.getContent());
//        model.addAttribute("pages", showPages);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", lectures.getTotalPages());
//        model.addAttribute("instructor", instructor);
//    }

//    private Instructor findInstructor(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Instructor instructor = memberService.findInstructor(memberId);
//        return instructor;
//    }
//    private Student findStudent(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Student student = memberService.findStudent(memberId);
//        return student;
//    }
}
