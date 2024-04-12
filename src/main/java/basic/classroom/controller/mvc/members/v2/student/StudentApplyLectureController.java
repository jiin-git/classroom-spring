package basic.classroom.controller.mvc.members.v2.student;

import basic.classroom.domain.LectureSearchCondition;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.LectureStatusSearchCondition;
import basic.classroom.dto.SearchLectureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/student/find/lectures")
public class StudentApplyLectureController {
    @GetMapping("")
    public String findAndApplyLectures(@ModelAttribute SearchLectureRequest searchLectureRequest, Model model) {
        addLectureStatusToModel(model);
        addSearchConditionToModel(model);
        return "member/student/v2/findLecture";
    }
    private void addSearchConditionToModel(Model model) {
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
    }
    private void addLectureStatusToModel(Model model) {
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
    }
}
