package basic.classroom.controller.api.members.student;

import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.service.members.student.StudentLectureJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static basic.classroom.dto.LectureResponse.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members/student/lectures")
public class StudentMyLecturesApi {
    private final StudentLectureJpaService studentLectureService;

    @PostMapping("/{lectureId}")
    public ResponseEntity<Long> addLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        Long mapperId = studentLectureService.applyLecture(loginId, lectureId);
        return ResponseEntity.ok(mapperId);
    }

    @GetMapping("")
    public ResponseEntity<Page<StudentLectureBasicResponse>> getMyLectures(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) Long page) {
        String loginId = userDetails.getUsername();
        Page<StudentLectureBasicResponse> myLectures = studentLectureService.findMyLectures(loginId, page);
        return ResponseEntity.ok(myLectures);
    }

    @GetMapping("/{mapperId}")
    public ResponseEntity<StudentLectureDetailsResponse> getMyLectureInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long mapperId) {
        String loginId = userDetails.getUsername();
        StudentLectureDetailsResponse lectureInfo = studentLectureService.getLectureInfo(loginId, mapperId);
        return ResponseEntity.ok(lectureInfo);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<StudentFindLecturesResponse>> findLecturesBySearchCondition(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute SearchLectureRequest searchLectureRequest) {
        String loginId = userDetails.getUsername();
        Page<StudentFindLecturesResponse> responseLectures = studentLectureService.findPersonalizedLectures(loginId, searchLectureRequest);
        return ResponseEntity.ok(responseLectures);
    }

    @DeleteMapping("/{mapperId}")
    public ResponseEntity<Void> cancelMyLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long mapperId) {
        String loginId = userDetails.getUsername();
        studentLectureService.cancelLecture(loginId, mapperId);
        return ResponseEntity.noContent().build();
    }
}
