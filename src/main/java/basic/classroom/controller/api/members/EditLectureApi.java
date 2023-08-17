package basic.classroom.controller.api.members;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.dto.UpdateLectureDto;
import basic.classroom.service.datajpa.LectureJpaService;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class EditLectureApi {
    private final LectureJpaService lectureService;
    private final MemberJpaService memberService;

    @PatchMapping("/instructors/{id}/lectures/{lectureId}")
    public ResponseEntity<Lecture> editLecture(@PathVariable Long id, @PathVariable Long lectureId, @Validated @ModelAttribute("lecture") UpdateLectureDto lectureDto) {
        Instructor instructor = memberService.findInstructor(id);
        Lecture lecture = lectureService.findOne(lectureId);

        int updatePersonnel = lectureDto.getPersonnel();
        int personnel = lecture.getPersonnel();
        int remainingPersonnel = lecture.getRemainingPersonnel();
        int appliedPersonnel = personnel - remainingPersonnel;
        int updateRemainingPersonnel = updatePersonnel - appliedPersonnel;

        lectureService.updateLecture(instructor, lectureDto, updateRemainingPersonnel);
        Lecture updateLecture = lectureService.findOne(id);


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/lectures/" + lectureId));

        return ResponseEntity.ok()
                .headers(headers)
                .body(updateLecture);

//        return "redirect:/instructor/lectures";
    }
}
