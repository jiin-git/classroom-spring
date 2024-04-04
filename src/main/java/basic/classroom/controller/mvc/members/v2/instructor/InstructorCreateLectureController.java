package basic.classroom.controller.mvc.members.v2.instructor;

import basic.classroom.domain.LectureStatus;
import basic.classroom.dto.AddLectureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/instructor/create/lectures")
public class InstructorCreateLectureController {
    @GetMapping("")
    public String createLectureForm(Model model) {
        model.addAttribute("createLectureForm", new AddLectureRequest());
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);

        return "member/instructor/v2/createLecture";
    }
}
