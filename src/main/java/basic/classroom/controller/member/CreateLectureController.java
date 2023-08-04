package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.Instructor;
import basic.classroom.domain.LectureStatus;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreateLectureController {
    private final LectureService lectureService;
    private final MemberService memberService;

    @GetMapping("/instructor/create/lecture")
    public String createLectureForm(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);

        model.addAttribute("instructor", instructor);
        model.addAttribute("createLectureForm", new AddLectureDto());
        addModeTolLectureStatus(model);

        return "member/instructor/createLecture";
    }

    @PostMapping("/instructor/create/lecture")
    public String createLecture(@Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto, BindingResult bindingResult, HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            model.addAttribute("instructor", instructor);
            addModeTolLectureStatus(model);
            return "member/instructor/createLecture";
        }

        // 성공 로직
        lectureService.create(instructor, lectureDto);
        return "redirect:/instructor/lectures";
    }

    private static void addModeTolLectureStatus(Model model) {
        LectureStatus[] lectureStatusList = LectureStatus.values();
        model.addAttribute("lectureStatusList", lectureStatusList);
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
    }

    private Instructor findInstructor(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Instructor instructor = memberService.findInstructor(memberId);
        return instructor;
    }
}
