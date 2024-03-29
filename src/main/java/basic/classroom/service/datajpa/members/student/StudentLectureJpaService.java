package basic.classroom.service.datajpa.members.student;

import basic.classroom.domain.*;
import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.LectureNotFoundException;
import basic.classroom.repository.dataJpa.LectureJpaRepository;
import basic.classroom.repository.dataJpa.LectureStudentMapperJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentLectureJpaService {
    private final StudentJpaRepository studentJpaRepository;
    private final LectureJpaRepository lectureJpaRepository;
    private final LectureStudentMapperJpaRepository mapperJpaRepository;

    @Transactional
    public Long applyLecture(Student student, Long lectureId) {
        Lecture lecture = lectureJpaRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }

        LectureStudentMapper mapper = LectureStudentMapper.builder().build();
        LectureStudentMapper savedMapper = mapperJpaRepository.save(mapper);
        student.applyLecture(savedMapper, lecture);

        return savedMapper.getId();
    }

    @Transactional
    public void cancelLecture(Student student, Long mapperId) {
        LectureStudentMapper mapper = student.getMapper(mapperId);
        Lecture lecture = mapper.getLecture();

        student.cancelLecture(mapperId, lecture);
        mapperJpaRepository.delete(mapper);
    }

    public Lecture findMyLecture(Student student, Long mapperId) {
        return student.getLecture(mapperId);
    }
    public Page<Lecture> findMyLecturesByPage(Long studentId, Long page) {
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(studentId, pageable);
        return lectures;
    }

    public Page<Lecture> findPersonalizedLectures(SearchLectureRequest searchLectureRequest) {
        int page = searchLectureRequest.getPage();
        int pageSize = 10;
        String status = searchLectureRequest.getStatus();
        String condition = searchLectureRequest.getCondition();
        String text = searchLectureRequest.getText();
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if (status != null && !status.isBlank() && !status.equals("ALL")) {
            LectureStatus lectureStatus = LectureStatus.valueOf(searchLectureRequest.getStatus());
            if (condition != null && !condition.isBlank()) {
                return findByAllConditions(searchLectureRequest, pageable);
            }
            return findByLectureStatus(lectureStatus, pageable);
        }
        if (condition != null && !condition.isBlank()) {
            return findByCondition(condition, text, pageable);
        }
        return findAllLectures(pageable);
    }
    private Page<Lecture> findAllLectures(Pageable pageable) {
        return lectureJpaRepository.findAll(pageable);
    }
    private Page<Lecture> findByAllConditions(SearchLectureRequest searchLectureRequest, Pageable pageable) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(searchLectureRequest.getCondition());
        LectureStatus lectureStatus = LectureStatus.valueOf(searchLectureRequest.getStatus());
        String text = searchLectureRequest.getText();

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            return lectureJpaRepository.findByLectureStatusAndInstructor_Member_Name(lectureStatus, text, pageable);
        }

        return lectureJpaRepository.findByLectureStatusAndName(lectureStatus, text, pageable);
    }
    private Page<Lecture> findByLectureStatus(LectureStatus lectureStatus, Pageable pageable) {
        return lectureJpaRepository.findByLectureStatus(lectureStatus, pageable);
    }
    private Page<Lecture> findByCondition(String condition, String text, Pageable pageable) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            return lectureJpaRepository.findByInstructor_Member_Name(text, pageable);
        }
        return lectureJpaRepository.findByName(text, pageable);
    }
}
