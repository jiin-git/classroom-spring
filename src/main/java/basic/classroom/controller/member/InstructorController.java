package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.UpdateLectureDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.service.InstructorService;
import basic.classroom.service.LectureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final LectureService lectureService;

    @GetMapping("/instructor/lectures/{id}")
    public String myLecture(@PathVariable Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);

        Instructor instructor = instructorService.findOne(id);
        List<Lecture> lectures = instructorService.findLectures(instructor.getId());

        model.addAttribute("instructor", instructor);
        model.addAttribute("lectures", lectures);

        log.info("session={}", session.getAttribute(SessionConst.LOGIN_ID));

        return "member/instructor/lectureList";
    }

    @GetMapping("/instructor/create/lecture/{id}")
    public String createLectureForm(@PathVariable Long id, Model model) {
        Instructor instructor = instructorService.findOne(id);
        LectureStatus[] lectureStatusList = LectureStatus.values();

        model.addAttribute("instructor", instructor);
        model.addAttribute("createLectureForm", new AddLectureDto());
        model.addAttribute("lectureStatusList", lectureStatusList);

        return "member/instructor/createLecture";
    }

    @PostMapping("/instructor/create/lecture/{id}")
    public String createLecture(@PathVariable Long id,
                                @Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto, BindingResult bindingResult, Model model) {

        // 검증 로직
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            Instructor instructor = instructorService.findOne(id);
            LectureStatus[] lectureStatusList = LectureStatus.values();

            model.addAttribute("instructor", instructor);
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/createLecture";
        }

        // 성공 로직
        Long lectureId = instructorService.addLecture(id, lectureDto);
        log.info("lectureDto.lectureStatus={}", lectureDto.getLectureStatus());

        return "redirect:/instructor/lectures/{id}";
    }

    @GetMapping("/instructor/edit/lectures/{id}")
    public String editLectureForm(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findOne(id);
        UpdateLectureDto lectureDto = new UpdateLectureDto(lecture);
        LectureStatus[] lectureStatusList = LectureStatus.values();

        model.addAttribute("lecture", lectureDto);
        model.addAttribute("lectureStatusList", lectureStatusList);

        return "member/instructor/editLectureForm";
    }

    @PostMapping("/instructor/edit/lectures/{id}")
    public String editLecture(@PathVariable Long id,
                              @Validated @ModelAttribute("lecture") UpdateLectureDto lectureDto,
                              BindingResult bindingResult, Model model) {

        // 검증 로직
        if (bindingResult.hasErrors()) {
            LectureStatus[] lectureStatusList = LectureStatus.values();
            model.addAttribute("lectureStatusList", lectureStatusList);

            return "member/instructor/editLectureForm";
        }

        // 성공 로직
        instructorService.updateLecture(lectureDto.getInstructorId(), lectureDto);
        return "redirect:/instructor/lectures/" + lectureDto.getInstructorId();
    }

    @GetMapping("/instructor/mypage/{id}")
    public String myPage(@PathVariable Long id, Model model) {
        Instructor instructor = instructorService.findOne(id);
        model.addAttribute("instructor", instructor);

        return "member/instructor/myPage";
    }

    @GetMapping("/instructor/update/mypage/{id}")
    public String updateMyPageForm(@PathVariable Long id, Model model) {
        Instructor instructor = instructorService.findOne(id);
        UpdateMemberDto memberDto = new UpdateMemberDto(instructor);

        model.addAttribute("id", id);
        model.addAttribute("instructor", memberDto);

        return "member/instructor/updateMyPage";
    }

    @PostMapping("/instructor/update/mypage/{id}")
    public String updateMyPage(@PathVariable Long id,
                               @Validated @ModelAttribute("instructor") UpdateMemberDto updateParam,
                               BindingResult bindingResult, Model model) {
        // 검증 로직
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "member/instructor/updateMyPage";
        }

        // 성공 로직
        instructorService.update(id, updateParam);
        return "redirect:/instructor/mypage/" + id;
    }
}
