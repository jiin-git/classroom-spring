package basic.classroom.controller.mvc.members.v2.student;

import basic.classroom.domain.LectureSearchCondition;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.LectureStatusSearchCondition;
import basic.classroom.dto.SearchLectureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/student/find/lectures")
public class StudentApplyLectureController {
    @GetMapping("")
    public String findAndApplyLectures(@ModelAttribute SearchLectureRequest searchLectureRequest, BindingResult bindingResult, Model model) {
        validateSearchCondition(searchLectureRequest, bindingResult);
        addLectureStatusToModel(model);
        addSearchConditionToModel(model, searchLectureRequest);
        return "member/student/v2/findLecture";
    }

    private void validateSearchCondition(SearchLectureRequest searchLectureRequest, BindingResult bindingResult) {
        String condition = searchLectureRequest.getCondition();
        String text = searchLectureRequest.getText();
        if (condition != null && !condition.isBlank()) {
            if (text.isBlank()) {
//                ErrorCode errorCode = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION;
//                String errorCodeName = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION.name();
//                String errorMessage = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION.getMessage();
//                bindingResult.rejectValue("text", errorCodeName, new Object[]{errorCode}, errorMessage);
//                bindingResult.rejectValue("text", "NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
            }
        }

        if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
//                ErrorCode errorCode = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION;
//                String errorCodeName = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION.name();
//                String errorMessage = ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION.getMessage();
//                bindingResult.rejectValue("text", errorCodeName, new Object[]{errorCode}, errorMessage);
//                bindingResult.rejectValue("condition","NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
            }
        }
    }

    private void addSearchConditionToModel(Model model, SearchLectureRequest searchLectureRequest) {
        String status = searchLectureRequest.getStatus();
        String condition = searchLectureRequest.getCondition();

        if (status != null && !status.isBlank() || condition != null && !condition.isBlank()) {
            model.addAttribute("searchLectureRequest", searchLectureRequest);
        }
        else {
            model.addAttribute("searchLectureRequest", new SearchLectureRequest());
        }

        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
    }
    private void addLectureStatusToModel(Model model) {
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
    }
}
