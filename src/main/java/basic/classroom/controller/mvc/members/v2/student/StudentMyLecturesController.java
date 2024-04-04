package basic.classroom.controller.mvc.members.v2.student;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/student/lectures")
public class StudentMyLecturesController {
    @GetMapping("")
    public String myLectures(@RequestParam(required = false) Long page) {
        return "member/student/v2/lectureList";
    }

    @GetMapping("/{lectureId}")
    public String myLectureInfo(@PathVariable Long lectureId) {
        return "member/student/v2/lectureInfo";
    }
}
