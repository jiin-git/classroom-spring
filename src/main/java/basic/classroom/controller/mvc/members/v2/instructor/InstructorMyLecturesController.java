package basic.classroom.controller.mvc.members.v2.instructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/instructor/lectures")
public class InstructorMyLecturesController {
    @GetMapping("")
    public String myLectures(@RequestParam(required = false) Long page) {
        return "member/instructor/v2/lectureList";
    }

    @GetMapping("/{lectureId}")
    public String myLectureInfo(@PathVariable Long lectureId) {
        return "member/instructor/v2/lectureInfo";
    }

    @GetMapping("/{lectureId}/applicants")
    public String myLectureApplicantsList(@PathVariable Long lectureId) {
        return "member/instructor/v2/applicantsInfo";
    }
}
