package basic.classroom.controller.members.v0;

import basic.classroom.controller.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.service.jpa.LectureService;
import basic.classroom.service.jpa.MemberService;
import basic.classroom.service.PagingService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@Slf4j
//@Controller
@RequiredArgsConstructor
public class ApplyLectureController {
    private final MemberService memberService;
    private final LectureService lectureService;
    private final PagingService pagingService;

//    @GetMapping("/student/find/lectures")
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

    private String AddModelToShowFindLecturesDto(Model model, Student student, SearchConditionDto searchConditionDto, ShowFindLecturesDto showFindLecturesDto) {
        addModelToStudentAndLectures(model, student, showFindLecturesDto.getShowLectures());
        addModelToPages(model, showFindLecturesDto.getShowPages(), showFindLecturesDto.getEndPage());
        addModelToLectureStatus(model);
        addModelToSearchCondition(searchConditionDto, model);

        return "member/student/findLecture";
    }

    private void addModelToStudentAndLectures(Model model, Student student, List<Lecture> showLectures) {
        model.addAttribute("student", student);
        model.addAttribute("lectures", showLectures);
    }

    private void addModelToSearchCondition(SearchConditionDto searchConditionDto, Model model) {
        model.addAttribute("searchConditionDto", searchConditionDto);
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
    }

    private void addModelToPages(Model model, List<Integer> showPages, int pageSize) {
        int startPage = 1;
        int endPage = pageSize;

        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    private void addModelToLectureStatus(Model model) {
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
    }

    private int getEndPage(int lecturesCnt) {
        return (int) Math.ceil((double) lecturesCnt / 10);
    }
}