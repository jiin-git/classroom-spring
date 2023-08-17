package basic.classroom.controller.members.v2;

import basic.classroom.domain.LectureSearchCondition;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.LectureStatusSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplyLectureJpaController {

    @GetMapping("/student/find/lectures")
    public String findLectures(Model model) {
        addLectureStatusToModel(model);
        addSearchConditionToModel(model);
        return "member/student/findLecture";
    }

//    @PostMapping("/student/add/lecture/{lectureId}")
//    public String addLecture(@PathVariable Long lectureId, HttpSession session) {
//        Student student = findStudent(session);
//        lectureService.applyLecture(student, lectureId);
//        return "redirect:/student/lectures";
//    }

//    @PostMapping("/student/cancel/lecture/{lectureId}")
//    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
//        Student student = findStudent(session);
//        lectureService.cancelLecture(student, lectureId);
//        return "redirect:/student/lectures";
//    }

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
