package basic.classroom.controller.member;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final LectureService lectureService;

    // 추후 pathvariable -> json 으로 변경해보기
    @GetMapping("/student/lectures/{id}")
    public String myLecture(@PathVariable Long id, Model model) {
        Student student = studentService.findOne(id);
        List<Lecture> lectures = studentService.findAllLectures(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);

        return "member/student/lectureList";
    }

    @PostMapping("/student/cancel/lecture/{studentId}/{lectureId}")
    public String cancelLecture(@PathVariable Long studentId, @PathVariable Long lectureId) {
        studentService.cancelLecture(studentId, lectureId);
        return "redirect:/student/lectures/" + studentId;
    }

    @GetMapping("/student/find/lecture/{id}")
    public String findLecture(@PathVariable Long id, Model model) {
        Student student = studentService.findOne(id);
        List<Lecture> lectures = lectureService.findAll();

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);

        return "member/student/findLecture";
    }

    @PostMapping("/student/find/lecture/{studentId}/{lectureId}")
    public String addLecture(@PathVariable Long studentId, @PathVariable Long lectureId, Model model) {
        studentService.addLecture(studentId, lectureId);
        return "redirect:/student/lectures/" + studentId;
    }

    @GetMapping("/student/mypage/{id}")
    public String myPage(@PathVariable Long id, Model model) {
        Student student = studentService.findOne(id);
        model.addAttribute("student", student);

        return "member/student/myPage";
    }

    @GetMapping("/student/update/mypage/{id}")
    public String updateMyPageForm(@PathVariable Long id, Model model) {
        Student student = studentService.findOne(id);
        UpdateMemberDto memberDto = new UpdateMemberDto(student);

        model.addAttribute("id", id);
        model.addAttribute("student", memberDto);

        return "member/student/updateMyPage";
    }

    @PostMapping("/student/update/mypage/{id}")
    public String updateMyPage(@PathVariable Long id,
                               @Validated @ModelAttribute("student") UpdateMemberDto updateParam,
                               BindingResult bindingResult, Model model) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "member/student/updateMyPage";
        }

        // 성공 로직
        studentService.update(id, updateParam);
        return "redirect:/student/mypage/" + id;
    }
}
