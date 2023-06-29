package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.UpdateLectureDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.dto.UpdatePwDto;
import basic.classroom.service.InstructorService;
import basic.classroom.service.LectureService;
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

    @GetMapping("/instructor/lectures")
    public String myLecture(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        List<Lecture> lectures = instructorService.findLectures(instructor.getId());

        model.addAttribute("instructor", instructor);
        model.addAttribute("lectures", lectures);

        return "member/instructor/lectureList";
    }

    @GetMapping("/instructor/create/lecture")
    public String createLectureForm(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        LectureStatus[] lectureStatusList = LectureStatus.values();

        model.addAttribute("instructor", instructor);
        model.addAttribute("createLectureForm", new AddLectureDto());
        model.addAttribute("lectureStatusList", lectureStatusList);

        return "member/instructor/createLecture";
    }

    @PostMapping("/instructor/create/lecture")
    public String createLecture(@Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto, BindingResult bindingResult, HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            LectureStatus[] lectureStatusList = LectureStatus.values();

            model.addAttribute("instructor", instructor);
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/createLecture";
        }

        // 성공 로직
        instructorService.addLecture(instructor.getId(), lectureDto);
        return "redirect:/instructor/lectures";
    }

    @GetMapping("/instructor/edit/lecture/{lectureId}")
    public String editLectureForm(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        UpdateLectureDto lectureDto = new UpdateLectureDto(lecture);
        LectureStatus[] lectureStatusList = LectureStatus.values();

        model.addAttribute("lecture", lectureDto);
        model.addAttribute("lectureStatusList", lectureStatusList);

        return "member/instructor/editLectureForm";
    }

    @PostMapping("/instructor/edit/lecture/{lectureId}")
    public String editLecture(@PathVariable Long lectureId,
                              @Validated @ModelAttribute("lecture") UpdateLectureDto lectureDto, BindingResult bindingResult, Model model) {

        // 검증 로직
        if (bindingResult.hasErrors()) {
            LectureStatus[] lectureStatusList = LectureStatus.values();
            model.addAttribute("lectureStatusList", lectureStatusList);

            return "member/instructor/editLectureForm";
        }

        // 성공 로직
        instructorService.updateLecture(lectureDto.getInstructorId(), lectureDto);
        return "redirect:/instructor/lectures";
    }

    @GetMapping("/instructor/mypage")
    public String myPage(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        model.addAttribute("instructor", instructor);

        return "member/instructor/myPage";
    }

    @GetMapping("/instructor/update/mypage")
    public String updateMyPageForm(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        UpdateMemberDto memberDto = new UpdateMemberDto(instructor);

        model.addAttribute("instructor", memberDto);

        return "member/instructor/updateMyPage";
    }

    @PostMapping("/instructor/update/mypage")
    public String updateMyPage(@Validated @ModelAttribute("instructor") UpdateMemberDto updateParam, BindingResult bindingResult, HttpSession session) {
        Instructor instructor = findInstructor(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/instructor/updateMyPage";
        }

        // 성공 로직
        instructorService.update(instructor.getId(), updateParam);
        return "redirect:/instructor/mypage";
    }

    @GetMapping("/instructor/update/pw")
    public String updatePwForm(Model model) {
        model.addAttribute("pwForm", new UpdatePwDto());
        return "member/instructor/updatePw";
    }

    @PostMapping("/instructor/update/pw")
    public String updatePw(@Validated @ModelAttribute("pwForm") UpdatePwDto updateParam, BindingResult bindingResult, HttpSession session) {
        Instructor instructor = findInstructor(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/instructor/updatePw";
        }

        if (!updateParam.getPassword().equals(updateParam.getCheckPassword())) {
            bindingResult.reject("notMatchPassword", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            return "member/instructor/updatePw";
        }

        // 성공 로직
        instructorService.updatePassword(instructor.getId(), updateParam.getPassword());
        return "redirect:/instructor/mypage";
    }

    private Instructor findInstructor(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Instructor instructor = instructorService.findOne(memberId);

        return instructor;
    }
}
