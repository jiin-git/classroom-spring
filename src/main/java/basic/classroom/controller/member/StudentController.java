package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.dto.UpdatePwDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.PagingService;
import basic.classroom.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final LectureService lectureService;
    private final PagingService pagingService;

    @GetMapping("/student/lectures")
    public String pagingMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = studentService.findAllLectures(student.getId());

        int lecturesCnt = lectures.size();
        int pageSize = pagingService.getPageSize(lecturesCnt);
        int currentPage = 1;
        int startPage = 1;
        int endPage = pageSize;

        if (page != null) {
            currentPage = page.intValue();
        }

        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);

        model.addAttribute("student", student);
        model.addAttribute("lectures", showLectures);
        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "member/student/lectureList";
    }

    @GetMapping("/student/lecture/{lectureId}")
    public String lectureInfo(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        model.addAttribute("lecture", lecture);

        return "member/student/lectureInfo";
    }

    @PostMapping("/student/cancel/lecture/{lectureId}")
    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        studentService.cancelLecture(student.getId(), lectureId);

        return "redirect:/student/lectures";
    }

    @GetMapping("/student/find/lectures")
    public String findLectures(@ModelAttribute SearchConditionDto searchConditionDto, BindingResult bindingResult, HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = lectureService.findPersonalizedLectures(searchConditionDto);

        int lecturesCnt = lectures.size();
        int pageSize = pagingService.getPageSize(lecturesCnt);
        int currentPage = 1;
        int startPage = 1;
        int endPage = pageSize;

        if (searchConditionDto.getPage() != null) {
            currentPage = searchConditionDto.getPage().intValue();
        }

        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);

        // Validation
        String condition = searchConditionDto.getCondition();
        String text = searchConditionDto.getText();
        if (condition != null && !condition.isBlank()) {
            if (text.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
                return findLecturesTemplate(searchConditionDto, model, student, showLectures, showPages, startPage, endPage);
            }
        }

        if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
                return findLecturesTemplate(searchConditionDto, model, student, showLectures, showPages, startPage, endPage);

            }
        }

        return findLecturesTemplate(searchConditionDto, model, student, showLectures, showPages, startPage, endPage);
    }

    private static String findLecturesTemplate(SearchConditionDto searchConditionDto, Model model, Student student,
                                               List<Lecture> showLectures, List<Integer> showPages, int startPage, int endPage) {
        model.addAttribute("student", student);
        model.addAttribute("lectures", showLectures);
        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchConditionDto", searchConditionDto);
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());

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
    public String updateMyPage(@Validated @ModelAttribute("student") UpdateMemberDto updateParam, BindingResult bindingResult, HttpSession session) {
        Student student = findStudent(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/student/updateMyPage";
        }

        // 성공 로직
        studentService.update(student.getId(), updateParam);
        return "redirect:/student/mypage";
    }

    @GetMapping("/student/update/pw")
    public String updatePwForm(Model model) {
        model.addAttribute("pwForm", new UpdatePwDto());
        return "member/student/updatePw";
    }

    @PostMapping("/student/update/pw")
    public String updatePw(@Validated @ModelAttribute("pwForm") UpdatePwDto updateParam, BindingResult bindingResult, HttpSession session) {
        Student student = findStudent(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/student/updatePw";
        }

        if (!updateParam.getPassword().equals(updateParam.getCheckPassword())) {
            bindingResult.reject("notMatchPassword", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            return "member/student/updatePw";
        }

        // 성공 로직
        studentService.updatePassword(student.getId(), updateParam.getPassword());
        return "redirect:/student/mypage";
    }


    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = studentService.findOne(memberId);

        return student;
    }

}
