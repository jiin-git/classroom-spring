package basic.classroom.controller.members.v2;

import basic.classroom.domain.LectureStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EditLectureController {

    @GetMapping("/instructor/edit/lectures/{lectureId}")
    public String editLectureForm(@PathVariable Long lectureId, Model model) {
        model.addAttribute("lectureStatusList", LectureStatus.values());
        return "member/instructor/editLectureForm";
    }

//    @PostMapping("/instructor/edit/lecture/{lectureId}")
//    public String editLecture(@PathVariable Long lectureId,
//                              @Validated @ModelAttribute("lecture") UpdateLectureDto lectureDto, BindingResult bindingResult, Model model) {
//        Instructor instructor = memberService.findInstructor(lectureDto.getInstructorId());
//        Lecture lecture = lectureService.findOne(lectureId);
//        LectureStatus lectureStatus = lectureDto.getLectureStatus();
//
//        int updatePersonnel = lectureDto.getPersonnel();
//        int personnel = lecture.getPersonnel();
//        int remainingPersonnel = lecture.getRemainingPersonnel();
//        int appliedPersonnel = personnel - remainingPersonnel;
//        int updateRemainingPersonnel = updatePersonnel - appliedPersonnel;
//
//        // 검증 로직
//        if (updateRemainingPersonnel < 0) {
//            String errorCode = "NotChangePersonnel";
//            String message = "신청한 정원보다 적은 수로 정원을 변경할 수 없습니다.";
//            return rejectRequest(bindingResult, model, errorCode, message);
//        }
//
//        if (lectureStatus.equals(LectureStatus.READY) && !lecture.getAppliedStudents().isEmpty()) {
//            String errorCode = "NotChangeLectureStatusReady";
//            String message = "신청한 학생이 있어 강의를 준비 상태로 변경할 수 없습니다.";
//            return rejectRequest(bindingResult, model, errorCode, message);
//        }
//
//        if (lectureStatus.equals(LectureStatus.OPEN) && updateRemainingPersonnel == 0) {
//            String errorCode = "NotChangeLectureStatusOpen";
//            String message = "정원이 다 차서 강의를 열 수 없습니다. 강의 상태를 FULL로 변경해주세요.";
//            return rejectRequest(bindingResult, model, errorCode, message);
//        }
//
//        if (lectureStatus.equals(LectureStatus.FULL) && updateRemainingPersonnel != 0) {
//            String errorCode = "NotChangeLectureStatusFull";
//            String message = "정원이 다 차지 않아 강의 상태를 변경 할 수 없습니다.";
//            return rejectRequest(bindingResult, model, errorCode, message);
//        }
//
//        if (bindingResult.hasErrors()) {
//            LectureStatus[] lectureStatusList = LectureStatus.values();
//            model.addAttribute("lectureStatusList", lectureStatusList);
//            return "member/instructor/editLectureForm";
//        }
//
//        // 성공 로직
//        lectureService.updateLecture(instructor, lectureDto, updateRemainingPersonnel);
//        return "redirect:/instructor/lectures";
//    }

    private String rejectRequest(BindingResult bindingResult, Model model, String errorCode, String message) {
        LectureStatus[] lectureStatusList = LectureStatus.values();
        bindingResult.reject(errorCode, message);
        model.addAttribute("lectureStatusList", lectureStatusList);
        return "member/instructor/editLectureForm";
    }
}
