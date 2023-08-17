package basic.classroom.controller.api.members;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import basic.classroom.service.PagingService;
import basic.classroom.service.datajpa.LectureJpaService;
import basic.classroom.service.datajpa.MemberJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MyLectureApi {
    private final LectureJpaService lectureService;
    private final MemberJpaService memberService;
    private final PagingService pagingService;

    @GetMapping("/instructors/{id}/lectures")
    public Page<Lecture> getInstructorPagingLectures(@PathVariable("id") Long id, @RequestParam(required = false) Long page) {
        Instructor instructor = memberService.findInstructor(id);
        Page<Lecture> lectures = getPagingLectures(page, instructor);
        return lectures;
    }

    @GetMapping("/students/{id}/lectures")
    public Page<Lecture> getStudentPagingLectures(@PathVariable("id") Long id, @RequestParam(required = false) Long page) {
        Student student = memberService.findStudent(id);
        Page<Lecture> lectures = getPagingLectures(page, student);
        return lectures;
    }

    private void pagingMyLectures(Long page, Student student) {
        int startPage = 1;
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = lectureService.findMyLecturesByPage(student, pageable);
        List<Integer> showPages = pagingService.getShowPages(pageable, lectures.getTotalPages());
    }
    private void pagingMyLectures(Long page, Instructor instructor) {
        int startPage = 1;
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = lectureService.findMyLecturesByPage(instructor, pageable);
        List<Integer> showPages = pagingService.getShowPages(pageable, lectures.getTotalPages());
    }

    private Page<Lecture> getPagingLectures(Long page, Instructor instructor) {
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = lectureService.findMyLecturesByPage(instructor, pageable);
        return lectures;
    }
    private Page<Lecture> getPagingLectures(Long page, Student student) {
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = lectureService.findMyLecturesByPage(student, pageable);
        return lectures;
    }
}
