package basic.classroom.controller.api.members;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.dto.AddLectureDto;
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
public class CreateLectureApi {
    private final LectureJpaService lectureService;
    private final MemberJpaService memberService;

    @PostMapping("/instructors/{id}/lectures")
    public ResponseEntity<Lecture> createLecture(@PathVariable Long id, @Validated @ModelAttribute("createLectureForm") AddLectureDto lectureDto) {
        Instructor instructor = memberService.findInstructor(id);
        Long lectureId = lectureService.create(instructor, lectureDto);
        Lecture lecture = lectureService.findOne(lectureId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/instructor/lectures"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(lecture);
    }
}
