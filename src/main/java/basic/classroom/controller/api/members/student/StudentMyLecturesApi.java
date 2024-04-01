package basic.classroom.controller.api.members.student;

import basic.classroom.config.JwtTokenProvider;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.dto.LectureResponse.LectureBasicResponse;
import basic.classroom.dto.LectureResponse.LectureDetailsResponse;
import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.service.datajpa.members.MemberJpaServiceV2;
import basic.classroom.service.datajpa.members.student.StudentLectureJpaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/{lectureId}")
    public ResponseEntity<Long> addLecture(@PathVariable Long lectureId, HttpServletRequest request) {
        Student student = findStudent(request);
        Long mapperId = studentLectureService.applyLecture(student, lectureId);

        return ResponseEntity.ok(mapperId);
    }

    @GetMapping("")
    public ResponseEntity<Page<LectureBasicResponse>> getMyLectures(@RequestParam(required = false) Long page, HttpServletRequest request) {
        Student student = findStudent(request);
        Page<Lecture> lectures = studentLectureService.findMyLecturesByPage(student.getId(), page);
        Page<LectureBasicResponse> lectureResponseDtos = lectures.map(LectureBasicResponse::fromLecture);
        return new ResponseEntity<>(lectureResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<LectureDetailsResponse>> findLecturesBySearchCondition(@ModelAttribute SearchLectureRequest searchLectureRequest, HttpServletRequest request) {
//        CustomPage<LectureDetailsResponse> showLecturesInfo = getPagingLecturesInfo(searchLectureRequest);
        Student student = findStudent(request);
        Page<Lecture> personalizedLectures = studentLectureService.findPersonalizedLectures(searchLectureRequest);
        Page<LectureDetailsResponse> responsePersonalizedLectures = personalizedLectures.map(LectureDetailsResponse::fromLecture);
        return ResponseEntity.ok(responsePersonalizedLectures);
    }

    @GetMapping("/{mapperId}")
    public ResponseEntity<LectureDetailsResponse> getMyLectureInfo(@PathVariable Long mapperId, HttpServletRequest request) {
        Student student = findStudent(request);
        Lecture lecture = student.getLecture(mapperId);
        LectureDetailsResponse lectureDetailsResponse = LectureDetailsResponse.fromLecture(lecture);
        return ResponseEntity.ok(lectureDetailsResponse);
    }

    @GetMapping("/ids")
    public ResponseEntity<Set<Long>> getMyLecturesMapperIds(HttpServletRequest request) {
        Student student = findStudent(request);
        Set<Long> myLecturesMapperIds = student.getApplyingLectures().keySet();
        log.info("myLecturesIds = {}", myLecturesMapperIds);
        return ResponseEntity.ok(myLecturesMapperIds);
    }

    @DeleteMapping("/{mapperId}")
    public ResponseEntity<Void> cancelLecture(@PathVariable Long mapperId, HttpServletRequest request) {
        Student student = findStudent(request);
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

    private Student findStudent(HttpServletRequest request) {
        String jwtToken = jwtTokenProvider.getJWTToken(request);
        String loginId = jwtTokenProvider.getLoginId(jwtToken);
        Student student = memberService.findStudentByLoginId(loginId);
        return student;
    }
}
