package basic.classroom.controller.mvc.members.v2.instructor;

import basic.classroom.domain.LectureStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/instructor/edit/lectures/{lectureId}")
public class InstructorUpdateLectureController {
    @GetMapping("")
    public String updateLectureForm(@PathVariable Long lectureId, Model model) {
        model.addAttribute("lectureStatusList", LectureStatus.values());
        return "member/instructor/v2/editLectureForm";
    }
}
