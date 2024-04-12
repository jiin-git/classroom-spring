package basic.classroom.controller.api.members.instructor;

import basic.classroom.dto.AddLectureRequest;
import basic.classroom.dto.ApplicantsResponse;
import basic.classroom.dto.LectureResponse.InstructorLectureBasicResponse;
import basic.classroom.dto.LectureResponse.InstructorLectureDetailsResponse;
import basic.classroom.dto.UpdateLecture.UpdateLectureRequest;
import basic.classroom.service.members.instructor.InstructorLectureJpaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/instructor/lectures")
public class InstructorMyLecturesApi {
    private final InstructorLectureJpaService instructorLectureService;

    @PostMapping("")
    public ResponseEntity<Long> createLecture(@AuthenticationPrincipal UserDetails userDetails, @Validated @ModelAttribute("createLectureForm") AddLectureRequest addLectureRequest) {
        String loginId = userDetails.getUsername();
        Long lectureId = instructorLectureService.createLecture(loginId, addLectureRequest);
        return new ResponseEntity<>(lectureId, HttpStatus.SEE_OTHER);
    }

    @GetMapping("")
    public ResponseEntity<Page<InstructorLectureBasicResponse>> getInstructorMyLectures(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) Long page) {
        String loginId = userDetails.getUsername();
        Page<InstructorLectureBasicResponse> lectureBasicResponses = instructorLectureService.findMyLecturesByPage(loginId, page);
        return new ResponseEntity<>(lectureBasicResponses, HttpStatus.OK);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<InstructorLectureDetailsResponse> getLectureInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        InstructorLectureDetailsResponse lectureInfo = instructorLectureService.getLectureInfo(loginId, lectureId);
        return ResponseEntity.ok(lectureInfo);
    }

    @GetMapping("/{lectureId}/applicants")
    public ResponseEntity<List<ApplicantsResponse>> getApplicants(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        List<ApplicantsResponse> applicantsResponse = instructorLectureService.getApplicants(loginId, lectureId);
        return ResponseEntity.ok(applicantsResponse);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<Void> updateLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId, @Valid @ModelAttribute UpdateLectureRequest updateLectureRequest) {
        String loginId = userDetails.getUsername();
        instructorLectureService.updateLecture(loginId, lectureId, updateLectureRequest);
        return ResponseEntity.noContent().build();
    }
}