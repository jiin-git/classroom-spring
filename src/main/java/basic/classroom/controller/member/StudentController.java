package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.dto.UpdatePwDto;
import basic.classroom.service.LectureService;
import basic.classroom.service.StudentService;
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
public class StudentController {

    private final StudentService studentService;
    private final LectureService lectureService;

    @GetMapping("/student/lectures")
    public String myLecture(HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = studentService.findAllLectures(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);

        return "member/student/lectureList";
    }

    @PostMapping("/student/cancel/lecture/{lectureId}")
    public String cancelLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        studentService.cancelLecture(student.getId(), lectureId);

        return "redirect:/student/lectures";
    }

//    @GetMapping("/student/find/lectures")
//    public String findLectures(HttpSession session, Model model) {
//        Student student = findStudent(session);
//        List<Lecture> lectures = lectureService.findAll();
//
//        model.addAttribute("student", student);
//        model.addAttribute("lectures", lectures);
//        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
//        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());
//
//        return "member/student/findLecture";
//    }

    @GetMapping("/student/find/lectures")
    public String findLectures(@ModelAttribute SearchConditionDto searchConditionDto, BindingResult bindingResult, HttpSession session, Model model) {
        Student student = findStudent(session);
        List<Lecture> lectures = lectureService.findPersonalizedLectures(searchConditionDto);

        log.info("searchConditionDto = {}", searchConditionDto);
        log.info("lectureStatus = {}", searchConditionDto.getStatus());
        log.info("searchCondition = {}", searchConditionDto.getCondition());
        log.info("text = {}", searchConditionDto.getText());
        log.info("lectures = {}", lectures);

        // Validation
        String condition = searchConditionDto.getCondition();
        String text = searchConditionDto.getText();
        if (condition != null && !condition.isBlank()) {
            if (text.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색명을 함께 입력해주세요.");
                log.info("bindingResult Result = {}" , bindingResult.hasErrors());

                model.addAttribute("student", student);
                model.addAttribute("lectures", lectures);
                model.addAttribute("searchConditionDto", searchConditionDto);
                model.addAttribute("lectureStatusReady", LectureStatus.READY);
                model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
                model.addAttribute("lectureStatusFull", LectureStatus.FULL);
                model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
                model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());

                return "member/student/findLecture";
            }
        }

        if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
                bindingResult.reject("NoSuchFieldError", "조건 검색 시 검색 조건을 설정해주세요.");
                log.info("bindingResult Result = {}" , bindingResult.hasErrors());

                model.addAttribute("student", student);
                model.addAttribute("lectures", lectures);
                model.addAttribute("searchConditionDto", searchConditionDto);
                model.addAttribute("lectureStatusReady", LectureStatus.READY);
                model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
                model.addAttribute("lectureStatusFull", LectureStatus.FULL);
                model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
                model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());

                return "member/student/findLecture";
            }
        }

        model.addAttribute("student", student);
        model.addAttribute("lectures", lectures);
        model.addAttribute("searchConditionDto", searchConditionDto);
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
        model.addAttribute("lectureStatusFull", LectureStatus.FULL);
        model.addAttribute("lectureStatusList", LectureStatusSearchCondition.values());
        model.addAttribute("lectureSearchConditions", LectureSearchCondition.values());

        return "member/student/findLecture";
    }

    @PostMapping("/student/add/lecture/{lectureId}")
    public String addLecture(@PathVariable Long lectureId, HttpSession session) {
        Student student = findStudent(session);
        studentService.addLecture(student.getId(), lectureId);

        return "redirect:/student/lectures";
    }

    @GetMapping("/student/mypage")
    public String myPage(HttpSession session, Model model) {
        Student student = findStudent(session);
        model.addAttribute("student", student);

        return "member/student/myPage";
    }

    @GetMapping("/student/update/mypage")
    public String updateMyPageForm(HttpSession session, Model model) {
        Student student = findStudent(session);
        UpdateMemberDto memberDto = new UpdateMemberDto(student);

        model.addAttribute("student", memberDto);

        return "member/student/updateMyPage";
    }

    @PostMapping("/student/update/mypage")
    public String updateMyPage(@Validated @ModelAttribute("student") UpdateMemberDto updateParam, BindingResult bindingResult, HttpSession session) {
        Student student = findStudent(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/student/updateMyPage";
        }

        // 성공 로직
        studentService.update(student.getId(), updateParam);
        return "redirect:/student/mypage";
    }

    @GetMapping("/student/update/pw")
    public String updatePwForm(Model model) {
        model.addAttribute("pwForm", new UpdatePwDto());
        return "member/student/updatePw";
    }

    @PostMapping("/student/update/pw")
    public String updatePw(@Validated @ModelAttribute("pwForm") UpdatePwDto updateParam, BindingResult bindingResult, HttpSession session) {
        Student student = findStudent(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            return "member/student/updatePw";
        }

        if (!updateParam.getPassword().equals(updateParam.getCheckPassword())) {
            bindingResult.reject("notMatchPassword", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            return "member/student/updatePw";
        }

        // 성공 로직
        studentService.updatePassword(student.getId(), updateParam.getPassword());
        return "redirect:/student/mypage";
    }


    private Student findStudent(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
        Student student = studentService.findOne(memberId);

        return student;
    }

}
