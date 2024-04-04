package basic.classroom.controller.api.members.instructor;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.dto.AddLectureRequest;
import basic.classroom.dto.ApplicantsResponse;
import basic.classroom.dto.LectureResponse.LectureBasicResponse;
import basic.classroom.dto.LectureResponse.LectureDetailsResponse;
import basic.classroom.dto.UpdateLecture.UpdateLectureRequest;
import basic.classroom.service.datajpa.members.MemberJpaServiceV2;
import basic.classroom.service.datajpa.members.instructor.InstructorLectureJpaService;
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
    private final MemberJpaServiceV2 memberService;
    private final InstructorLectureJpaService instructorLectureService;

    @PostMapping("")
    public ResponseEntity<Long> createLecture(@AuthenticationPrincipal UserDetails userDetails, @Validated @ModelAttribute("createLectureForm") AddLectureRequest addLectureRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        Long lectureId = instructorLectureService.createLecture(instructor, addLectureRequest);

        return new ResponseEntity<>(lectureId, HttpStatus.SEE_OTHER);
    }

    @GetMapping("")
    public ResponseEntity<Page<LectureBasicResponse>> getInstructorMyLectures(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) Long page) {
        log.info("userDetails userName = {}", userDetails.getUsername());

        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);
        Page<Lecture> lectures = instructorLectureService.findMyLecturesByPage(instructor.getId(), page);
        Page<LectureBasicResponse> lectureResponseDtos = lectures.map(LectureBasicResponse::fromLecture);

        return new ResponseEntity<>(lectureResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailsResponse> getLectureInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);

        Lecture lecture = instructor.getLecture(lectureId);
        LectureDetailsResponse lectureDetailsResponse = LectureDetailsResponse.fromLecture(lecture);

        return ResponseEntity.ok(lectureDetailsResponse);
    }

    @GetMapping("/{lectureId}/applicants")
    public ResponseEntity<List<ApplicantsResponse>> getApplicants(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);

        Lecture lecture = instructor.getLecture(lectureId);
        List<ApplicantsResponse> applicantsResponse = lecture.getApplicantsResponse();

//        List<Student> applicants = lectureService.findApplicantsById(lectureId);
//        List<ApplicantsResponse> applicantsResponseList = getApplicantDtoList(applicants);
        return ResponseEntity.ok(applicantsResponse);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<Void> updateLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId, @Valid @RequestBody UpdateLectureRequest updateLectureRequest) {
        String loginId = userDetails.getUsername();
        Instructor instructor = memberService.findInstructorByLoginId(loginId);

        log.info("instructor id = {}", instructor.getId());
        log.info("lecture id = {}", lectureId);
        log.info("updateLectureRequest = {}", updateLectureRequest);

        instructorLectureService.updateLecture(instructor, lectureId, updateLectureRequest);

        return ResponseEntity.noContent().build();
    }

//    private Page<LectureBasicResponse> getLectureResponseDtos(Long page, Instructor instructor) {
//        int currentPage = page == null ? 1 : page.intValue();
//        int pageSize = 10;
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
//        Page<Lecture> lectures = lectureService.findMyLecturesByPage(instructor, pageable);
//        Page<LectureBasicResponse> lectureResponseDtos = lectures.map(LectureBasicResponse::fromLecture);
//
//        return lectureResponseDtos;
//    }
//    private List<ApplicantsResponse> getApplicantDtoList(List<Student> applicants) {
//        List<ApplicantsResponse> applicantsResponseList = applicants.stream().map(ApplicantsResponse::fromStudent).toList();
//        return applicantsResponseList;
//    }
//    @Getter
//    @NoArgsConstructor
//    private class LectureInfo {
//        private Lecture lecture;
//        private String instructorName;
//
//        public LectureInfo(Lecture lecture) {
//            this.lecture = lecture;
//            this.instructorName = lecture.getInstructor().getMember().getName();
//        }
//    }
//
//    @Getter
//    @NoArgsConstructor
//    private class MyLecturesIds {
//        private Set<Long> list;
//
//        public MyLecturesIds(Set<Long> myLecturesIds) {
//            this.list = myLecturesIds;
//        }
//    }
}