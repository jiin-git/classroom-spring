package basic.classroom.controller.api.members.student;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.dto.LectureResponse.LectureBasicResponse;
import basic.classroom.dto.LectureResponse.LectureDetailsResponse;
import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.service.datajpa.members.MemberJpaServiceV2;
import basic.classroom.service.datajpa.members.student.StudentLectureJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members/student/lectures")
public class StudentMyLecturesApi {
    private final MemberJpaServiceV2 memberService;
    private final StudentLectureJpaService studentLectureService;

    @PostMapping("/{lectureId}")
    public ResponseEntity<Long> addLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long lectureId) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        Long mapperId = studentLectureService.applyLecture(student, lectureId);

        return ResponseEntity.ok(mapperId);
    }

    @GetMapping("")
    public ResponseEntity<Page<LectureBasicResponse>> getMyLectures(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) Long page) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        Page<Lecture> lectures = studentLectureService.findMyLecturesByPage(student.getId(), page);
        Page<LectureBasicResponse> lectureResponseDtos = lectures.map(LectureBasicResponse::fromLecture);
        return new ResponseEntity<>(lectureResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<LectureDetailsResponse>> findLecturesBySearchCondition(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute SearchLectureRequest searchLectureRequest) {
//        CustomPage<LectureDetailsResponse> showLecturesInfo = getPagingLecturesInfo(searchLectureRequest);
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        Page<Lecture> personalizedLectures = studentLectureService.findPersonalizedLectures(searchLectureRequest);
        Page<LectureDetailsResponse> responsePersonalizedLectures = personalizedLectures.map(LectureDetailsResponse::fromLecture);
        return ResponseEntity.ok(responsePersonalizedLectures);
    }

    @GetMapping("/{mapperId}")
    public ResponseEntity<LectureDetailsResponse> getMyLectureInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long mapperId) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        Lecture lecture = student.getLecture(mapperId);
        LectureDetailsResponse lectureDetailsResponse = LectureDetailsResponse.fromLecture(lecture);
        return ResponseEntity.ok(lectureDetailsResponse);
    }

    @GetMapping("/ids")
    public ResponseEntity<Set<Long>> getMyLecturesMapperIds(@AuthenticationPrincipal UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        Set<Long> myLecturesMapperIds = student.getApplyingLectures().keySet();
        log.info("myLecturesIds = {}", myLecturesMapperIds);
        return ResponseEntity.ok(myLecturesMapperIds);
    }

    @DeleteMapping("/{mapperId}")
    public ResponseEntity<Void> cancelLecture(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long mapperId) {
        String loginId = userDetails.getUsername();
        Student student = memberService.findStudentByLoginId(loginId);
        studentLectureService.cancelLecture(student, mapperId);
        return ResponseEntity.noContent().build();
    }

//    private CustomPage<LectureDetailsResponse> getPagingLecturesInfo(SearchLectureRequest searchLectureRequest) {
//        Integer page = searchLectureRequest.getPage();
//        int currentPage = page == null ? 1 : page.intValue();
//        int pageSize = 10;
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
//        Page<Lecture> lectures = lectureService.findPersonalizedLectures(searchLectureRequest, pageSize);
//        List<Integer> showPages = pagingService.getShowPages(pageable, lectures.getTotalPages());
//        List<LectureDetailsResponse> lectureInfoList = getLectureInfoList(lectures.getContent());
//        CustomPage<LectureDetailsResponse> showLectures = new CustomPage<>(lectureInfoList, showPages, currentPage, lectures.getTotalPages(), lectures.getTotalElements());
//
//        return showLectures;
//    }
//    private List<LectureDetailsResponse> getLectureInfoList(List<Lecture> lectures) {
//        List<LectureDetailsResponse> lectureInfoList = new ArrayList<>();
//        lectures.forEach(lecture -> {
//            LectureDetailsResponse lectureInfo = LectureDetailsResponse.fromLecture(lecture);
//            lectureInfoList.add(lectureInfo);
//        });
//        return lectureInfoList;
//    }
//
//    @Getter
//    @NoArgsConstructor
//    private class CustomPage<T> {
//        private List<LectureDetailsResponse> lectureInfoList;
//        private List<Integer> pages;
//        private int pageNumber;
//        private int totalPages;
//        private Long totalItems;
//
//        public CustomPage(List<LectureDetailsResponse> lectureInfoList, List<Integer> pages, int pageNumber, int totalPages, Long totalItems) {
//            this.lectureInfoList = lectureInfoList;
//            this.pages = pages;
//            this.pageNumber = pageNumber;
//            this.totalPages = totalPages;
//            this.totalItems = totalItems;
//        }
//    }
//
//    @Getter
//    @NoArgsConstructor
//    private class MyLecturesIds {
//        private Set<Long> list;
//        public MyLecturesIds(Set<Long> myLecturesIds) {
//            this.list = myLecturesIds;
//        }
//    }
}
