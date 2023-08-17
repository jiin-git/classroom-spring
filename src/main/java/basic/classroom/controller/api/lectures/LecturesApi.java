package basic.classroom.controller.api.lectures;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.service.datajpa.LectureJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LecturesApi {
    private final LectureJpaService lectureService;

    @GetMapping("")
    public ResponseEntity<Page<Lecture>> findLectures(@ModelAttribute SearchConditionDto searchConditionDto) {
        int pageSize = 10;
        Page<Lecture> lectures = lectureService.findPersonalizedLectures(searchConditionDto, pageSize);
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<Lecture> getLectureInfo(@PathVariable Long lectureId) {
        Lecture lecture = lectureService.findOne(lectureId);
        return ResponseEntity.ok(lecture);
    }

    @GetMapping("/{lectureId}/applicants")
    public ResponseEntity<List<Student>> getApplicants(@PathVariable Long lectureId) {
        List<Student> applicants = lectureService.findApplicantsById(lectureId);
        return ResponseEntity.ok(applicants);
    }
}
