package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.MemberService;
import basic.classroom.service.PagingService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplyLectureController {
    private final MemberService memberService;
    private final LectureService lectureService;
    private final PagingService pagingService;

//    @GetMapping("/student/find/lectures")
//    public String findLectures(@ModelAttribute SearchConditionDto searchConditionDto, BindingResult bindingResult, HttpSession session, Model model) {
//        Student student = findStudent(session);
//        List<Lecture> lectures = lectureService.findPersonalizedLectures(searchConditionDto);
//
//        int lecturesCnt = lectures.size();
//        int pageSize = pagingService.getPageSize(lectures.size());
//        int currentPage = searchConditionDto.getPage() == null ? 1 : searchConditionDto.getPage().intValue();
//
//        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
//        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);
//
//        ShowFindLecturesDto showFindLecturesDto = new ShowFindLecturesDto(pageSize, showLectures, showPages);
//
//        // Validation
//        String condition = searchConditionDto.getCondition();
//        String text = searchConditionDto.getText();
//        if (condition != null && !condition.isBlank()) {
//            if (text.isBlank()) {
//                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
//                return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
//            }
//        }
//
//        if (text != null && !text.isBlank()) {
//            if (condition == null || condition.isBlank()) {
//                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
//                return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
//            }
//        }
//
//        return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
//    }

    public int getEndPage(int lecturesCnt) {
        return (int) Math.ceil((double) lecturesCnt / 10);
    }

    @GetMapping("/student/find/lectures")
    public String findLectures(@ModelAttribute SearchConditionDto searchConditionDto, BindingResult bindingResult, HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = lectureService.findPersonalizedLectures(searchConditionDto);

        int lecturesCnt = lectures.size();
        int endPage = lectures.isEmpty() ? 1 : getEndPage(lectures.size());
        int currentPage = searchConditionDto.getPage() == null ? 1 : searchConditionDto.getPage().intValue();

        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);

        ShowFindLecturesDto showFindLecturesDto = new ShowFindLecturesDto(endPage, showLectures, showPages);

        // Validation
        String condition = searchConditionDto.getCondition();
        String text = searchConditionDto.getText();
        if (condition != null && !condition.isBlank()) {
            if (text.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
                return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
            }
        }

        if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
                return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
            }
        }

        return AddModelToShowFindLecturesDto(model, student, searchConditionDto, showFindLecturesDto);
    }
    @PostMapping("/student/add/lecture/{lectureId}")
    public String addLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        lectureService.applyLecture(student, lectureId);
        return "redirect:/student/lectures";
    }

    @PostMapping("/student/cancel/lecture/{lectureId}")
    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        lectureService.cancelLecture(student, lectureId);
        return "redirect:/student/lectures";
    }

//    @Getter
//    private class ShowFindLecturesDto {
//        private int pageSize;
//        private List<Lecture> showLectures;
//        private List<Integer> showPages;
//
//        public ShowFindLecturesDto(int pageSize, List<Lecture> showLectures, List<Integer> showPages) {
//            this.pageSize = pageSize;
//            this.showLectures = showLectures;
//            this.showPages = showPages;
//        }
//    }
    @Getter
    private class ShowFindLecturesDto {
        private int endPage;
        private List<Lecture> showLectures;
        private List<Integer> showPages;

        public ShowFindLecturesDto(int endPage, List<Lecture> showLectures, List<Integer> showPages) {
            this.endPage = endPage;
            this.showLectures = showLectures;
            this.showPages = showPages;
        }
    }

    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = memberService.findStudent(memberId);
        return student;
    }

//    private static String AddModelToShowFindLecturesDto(Model model, Student student, SearchConditionDto searchConditionDto, ShowFindLecturesDto showFindLecturesDto) {
//        addModelToStudentAndLectures(model, student, showFindLecturesDto.getShowLectures());
//        addModelToPages(model, showFindLecturesDto.getShowPages(), showFindLecturesDto.getPageSize());
//        addModelToLectureStatus(model);
//        addModelToSearchCondition(searchConditionDto, model);
//
//        return "member/student/findLecture";
//    }
    private static String AddModelToShowFindLecturesDto(Model model, Student student, SearchConditionDto searchConditionDto, ShowFindLecturesDto showFindLecturesDto) {
        addModelToStudentAndLectures(model, student, showFindLecturesDto.getShowLectures());
        addModelToPages(model, showFindLecturesDto.getShowPages(), showFindLecturesDto.getEndPage());
        addModelToLectureStatus(model);
        addModelToSearchCondition(searchConditionDto, model);

        return "member/student/findLecture";
    }

    private static void addModelToStudentAndLectures(Model model, Student student, List<Lecture> showLectures) {
        model.addAttribute("student", student);
        model.addAttribute("lectures", showLectures);
    }

    private static void addModelToSearchCondition(SearchConditionDto searchConditionDto, Model model) {
        model.addAttribute("searchConditionDto", searchConditionDto);
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
    }

    private static void addModelToPages(Model model, List<Integer> showPages, int pageSize) {
        int startPage = 1;
        int endPage = pageSize;

        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    private static void addModelToLectureStatus(Model model) {
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
    }
}
