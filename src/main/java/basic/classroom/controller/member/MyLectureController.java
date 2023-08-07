package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStudentMapper;
import basic.classroom.domain.Student;
import basic.classroom.dto.PageDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.MemberService;
import basic.classroom.service.PagingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyLectureController {
    private final LectureService lectureService;
    private final PagingService pagingService;
    private final MemberService memberService;

//    @GetMapping("/instructor/lectures")
//    public String instructorMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
//        Instructor instructor = findInstructor(session);
//        List<Lecture> lectures = lectureService.findAllLectures(instructor);
//
//        pagingLecturesModel(page, model, lectures);
//        model.addAttribute("instructor", instructor);
//
//        return "member/instructor/lectureList";
//    }
    @GetMapping("/instructor/lectures")
    public String instructorMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        pagingMyLectures(page, model, instructor);
        return "member/instructor/lectureList";
    }

//    @GetMapping("/student/lectures")
//    public String studentMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
//        Student student = findStudent(session);
//        List<Lecture> lectures = lectureService.findAllLectures(student);
//
//        pagingLecturesModel(page, model, lectures);
//        model.addAttribute("student", student);
//
//        return "member/student/lectureList";
//    }
    @GetMapping("/student/lectures")
    public String studentMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        Student student = findStudent(session);
        pagingMyLectures(page, model, student);
        return "member/student/lectureList";
    }

    @GetMapping("/instructor/lecture/{lectureId}")
    public String instructorLectureInfo(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        model.addAttribute("lecture", lecture);
        return "member/instructor/lectureInfo";
    }
    @GetMapping("/student/lecture/{lectureId}")
    public String studentLectureInfo(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        model.addAttribute("lecture", lecture);
        return "member/student/lectureInfo";
    }

    @GetMapping("/instructor/lecture/{lectureId}/applicants")
    public String applicantList(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        Collection<LectureStudentMapper> mappers = lecture.getAppliedStudents().values();
        List<Student> applicants = mappers.stream().map(lsm -> lsm.getStudent()).toList();

        model.addAttribute("applicants", applicants);
        return "member/instructor/applicantsInfo";
    }

//    private void pagingLecturesModel(Long page, Model model, List<Lecture> lectures) {
//        int lecturesCnt = lectures.size();
//        int pageSize = pagingService.getPageSize(lecturesCnt);
//        int currentPage = page == null? 1: page.intValue();
//        int startPage = 1;
//        int endPage = pageSize;
//
//        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
//        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);
//
//        model.addAttribute("lectures", showLectures);
//        model.addAttribute("pages", showPages);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//    }
    private void pagingMyLectures(Long page, Model model, Student student) {
        PageDto pageDto = getPageDto(page, student);

        List<Lecture> pagingLectures = lectureService.findByPageMyLectures(student, pageDto.getPage(), pageDto.getPageSize());
        List<Integer> showPages = pagingService.getShowPages(pageDto);

        addModelToPagingLecturesAndPages(model, pageDto, pagingLectures, showPages);
        model.addAttribute("student", student);
    }
    private void pagingMyLectures(Long page, Model model, Instructor instructor) {
        PageDto pageDto = getPageDto(page, instructor);

        List<Lecture> pagingLectures = lectureService.findByPageMyLectures(instructor, pageDto.getPage(), pageDto.getPageSize());
        List<Integer> showPages = pagingService.getShowPages(pageDto);

        addModelToPagingLecturesAndPages(model, pageDto, pagingLectures, showPages);
        model.addAttribute("instructor", instructor);
    }

    private PageDto getPageDto(Long page, Student student) {
        int pageSize = 10;
        int showPageUnit = 3;
        int lecturesCnt = student.getApplyingLectures().size();
        int currentPage = page == null? 1: page.intValue();
        int endPage = student.getApplyingLectures().isEmpty()? 1 : getMaxPage(lecturesCnt, pageSize);

        return new PageDto(currentPage, endPage, showPageUnit, pageSize);
    }
    private PageDto getPageDto(Long page, Instructor instructor) {
        int pageSize = 10;
        int showPageUnit = 3;
        int lecturesCnt = instructor.getLectures().size();
        int currentPage = page == null? 1: page.intValue();
        int endPage = instructor.getLectures().isEmpty()? 1 : getMaxPage(lecturesCnt, pageSize);

        return new PageDto(currentPage, endPage, showPageUnit, pageSize);
    }

    private void addModelToPagingLecturesAndPages(Model model, PageDto pageDto, List<Lecture> pagingLectures, List<Integer> showPages) {
        model.addAttribute("lectures", pagingLectures);
        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", pageDto.getStartPage());
        model.addAttribute("endPage", pageDto.getEndPage());
    }

    private static int getMaxPage(int lecturesCnt, int pageSize) {
        return (int) Math.ceil((double) lecturesCnt / (double) pageSize);
    }

    private Instructor findInstructor(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Instructor instructor = memberService.findInstructor(memberId);
        return instructor;
    }
    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = memberService.findStudent(memberId);
        return student;
    }

}
