package basic.classroom.controller.member;

import basic.classroom.controller.login.SessionConst;
import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.UpdateLectureDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.dto.UpdatePwDto;
import basic.classroom.service.InstructorService;
import basic.classroom.service.LectureService;
import basic.classroom.service.PagingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final LectureService lectureService;
    private final PagingService pagingService;

    @GetMapping("/instructor/lectures")
    public String pagingMyLecture(@RequestParam(required = false) Long page, HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);
        List<Lecture> lectures = instructorService.findLectures(instructor.getId());

        int lecturesCnt = lectures.size();
        int pageSize = pagingService.getPageSize(lecturesCnt);
        int currentPage = 1;
        int startPage = 1;
        int endPage = pageSize;

        if (page != null) {
            currentPage = page.intValue();
        }

        List<Lecture> showLectures = pagingService.filteringLectures(lectures, currentPage);
        List<Integer> showPages = pagingService.getShowPages(lecturesCnt, currentPage);

        model.addAttribute("instructor", instructor);
        model.addAttribute("lectures", showLectures);
        model.addAttribute("pages", showPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "member/instructor/lectureList";
    }

    @GetMapping("/instructor/lecture/{lectureId}")
    public String lectureInfo(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        model.addAttribute("lecture", lecture);

        return "member/instructor/lectureInfo";
    }

    @GetMapping("/instructor/lecture/{lectureId}/applicants")
    public String applicantList(@PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findOne(lectureId);
        Collection<LectureStudentMapper> mappers = lecture.getAppliedStudents().values();
        List<Student> applicants = mappers.stream().map(lsm -> lsm.getStudent()).toList();

        model.addAttribute("applicants", applicants);

        return "member/instructor/applicantsInfo";
    }

    @GetMapping("/instructor/create/lecture")
    public String createLectureForm(HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);

        model.addAttribute("instructor", instructor);
        model.addAttribute("createLectureForm", new AddLectureDto());
        addModelLectureStatus(model);

        return "member/instructor/createLecture";
    }

    @PostMapping("/instructor/create/lecture")
    public String createLecture(@Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto, BindingResult bindingResult, HttpSession session, Model model) {
        Instructor instructor = findInstructor(session);

        // 검증 로직
        if (bindingResult.hasErrors()) {
            model.addAttribute("instructor", instructor);
            addModelLectureStatus(model);

            return "member/instructor/createLecture";
        }

        // 성공 로직
        instructorService.addLecture(instructor.getId(), lectureDto);
        return "redirect:/instructor/lectures";
    }

    private static void addModelLectureStatus(Model model) {
        LectureStatus[] lectureStatusList = LectureStatus.values();
        model.addAttribute("lectureStatusList", lectureStatusList);
        model.addAttribute("lectureStatusReady", LectureStatus.READY);
        model.addAttribute("lectureStatusOpen", LectureStatus.OPEN);
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

        Lecture lecture = lectureService.findOne(lectureId);
        LectureStatus lectureStatus = lectureDto.getLectureStatus();
        LectureStatus[] lectureStatusList = LectureStatus.values();

        int updatePersonnel = lectureDto.getPersonnel();
        int personnel = lecture.getPersonnel();
        int remainingPersonnel = lecture.getRemainingPersonnel();
        int appliedPersonnel = personnel - remainingPersonnel;
        int updateRemainingPersonnel = updatePersonnel - appliedPersonnel;

        // 검증 로직
        if (updateRemainingPersonnel < 0) {
            bindingResult.reject("NotChangePersonnel", "신청한 정원보다 적은 수로 정원을 변경할 수 없습니다.");
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/editLectureForm";
        }

        if (lectureStatus.equals(LectureStatus.READY) && !lecture.getAppliedStudents().isEmpty()) {
            bindingResult.reject("NotChangeLectureStatusReady", "신청한 학생이 있어 강의를 준비 상태로 변경할 수 없습니다.");
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/editLectureForm";
        }

        if (lectureStatus.equals(LectureStatus.OPEN) && updateRemainingPersonnel == 0) {
            bindingResult.reject("NotChangeLectureStatusOpen", "정원이 다 차서 강의를 열 수 없습니다. 강의 상태를 FULL로 변경해주세요.");
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/editLectureForm";
        }

        if (lectureStatus.equals(LectureStatus.FULL) && updateRemainingPersonnel != 0) {
            bindingResult.reject("NotChangeLectureStatusFull", "정원이 다 차지 않아 강의 상태를 변경 할 수 없습니다.");
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/editLectureForm";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("lectureStatusList", lectureStatusList);
            return "member/instructor/editLectureForm";
        }

        // 성공 로직
        instructorService.updateLecture(lectureDto.getInstructorId(), lectureDto, updateRemainingPersonnel);
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
