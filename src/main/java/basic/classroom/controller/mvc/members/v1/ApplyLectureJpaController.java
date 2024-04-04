package basic.classroom.controller.mvc.members.v1;

import basic.classroom.controller.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.service.PagingService;
import basic.classroom.service.datajpa.LectureJpaService;
import basic.classroom.service.datajpa.MemberJpaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
//@Controller
@RequiredArgsConstructor
public class ApplyLectureJpaController {
    private final MemberJpaService memberService;
    private final LectureJpaService lectureService;
    private final PagingService pagingService;

//    @GetMapping("/student/find/lectures")
    public String findLectures(@ModelAttribute SearchLectureRequest searchLectureRequest, BindingResult bindingResult, HttpSession session, Model model) {
        Student student = findStudent(session);

        int pageSize = 10;
        Page<Lecture> lectures = lectureService.findPersonalizedLectures(searchLectureRequest, pageSize);
        List<Integer> showPages = pagingService.getShowPages(lectures.getPageable(), lectures.getTotalPages());

        // Validation
        validateSearchCondition(searchLectureRequest, bindingResult);

        addStudentAndLecturesToModel(model, student, lectures.getContent());
        addPagesToModel(model, showPages, lectures.getTotalPages());
        addLectureStatusToModel(model);
        addSearchConditionToModel(searchLectureRequest, model);

        return "member/student/findLecture";
    }

    private static void validateSearchCondition(SearchLectureRequest searchLectureRequest, BindingResult bindingResult) {
        String condition = searchLectureRequest.getCondition();
        String text = searchLectureRequest.getText();
        if (condition != null && !condition.isBlank()) {
            if (text.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
            }
        }

        if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
            }
        }
    }

    //    @PostMapping("/student/add/lecture/{lectureId}")
    public String addLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        lectureService.applyLecture(student, lectureId);
        return "redirect:/student/lectures";
    }

//    @PostMapping("/student/cancel/lecture/{lectureId}")
    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        lectureService.cancelLecture(student, lectureId);
        return "redirect:/student/lectures";
    }

    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = memberService.findStudent(memberId);
        return student;
    }

    private void addStudentAndLecturesToModel(Model model, Student student, List<Lecture> lectures) {
        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);
    }

    private void addSearchConditionToModel(SearchLectureRequest searchLectureRequest, Model model) {
        model.addAttribute("searchConditionDto", searchLectureRequest);
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
    }

    private void addPagesToModel(Model model, List<Integer> showPages, int pageSize) {
        int startPage = 1;
        int endPage = pageSize;

        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    private void addLectureStatusToModel(Model model) {
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
    }
}
