package basic.classroom.controller.members.v2;

import basic.classroom.domain.LectureStatus;
import basic.classroom.dto.AddLectureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreateLectureController {

    @GetMapping("/instructor/create/lecture")
    public String createLectureForm(Model model) {
        model.addAttribute("createLectureForm", new AddLectureDto());
        model.addAttribute("lectureStatusList", LectureStatus.values());
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);

        return "member/instructor/createLecture";
    }

//    @PostMapping("/instructor/create/lecture")
//    public String createLecture(@Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto, BindingResult bindingResult, HttpSession session, Model model) {
//        Instructor instructor = findInstructor(session);
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("instructor", instructor);
//            addModeTolLectureStatus(model);
//            return "member/instructor/createLecture";
//        }
//
//        // 성공 로직
//        lectureService.create(instructor, lectureDto);
//        return "redirect:/instructor/lectures";
//    }

}
