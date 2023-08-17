package basic.classroom.controller.api.members;

import basic.classroom.domain.*;
import basic.classroom.service.datajpa.LectureJpaService;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class ApplyLectureApi {
    private final MemberJpaService memberService;
    private final LectureJpaService lectureService;

    @PostMapping("/students/{id}/lectures/{lectureId}")
    public ResponseEntity<Lecture> addLecture(@PathVariable Long id, @PathVariable Long lectureId) {
        Student student = memberService.findStudent(id);
        Long appliedLectureId = lectureService.applyLecture(student, lectureId);
        Lecture lecture = lectureService.findOne(appliedLectureId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/lectures"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(lecture);
    }

    @DeleteMapping("/students/{id}/lectures/{lectureId}")
    public ResponseEntity<Void> cancelLecture(@PathVariable Long id, @PathVariable Long lectureId) {
        Student student = memberService.findStudent(id);
        lectureService.cancelLecture(student, lectureId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/student/lectures"));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }
}
