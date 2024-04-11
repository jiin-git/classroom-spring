package basic.classroom.service.members.student;

import basic.classroom.domain.*;
import basic.classroom.dto.SearchLectureRequest;
import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.LectureNotFoundException;
import basic.classroom.exception.LectureSearchParameterException;
import basic.classroom.repository.dataJpa.LectureJpaRepository;
import basic.classroom.repository.dataJpa.LectureStudentMapperJpaRepository;
import basic.classroom.service.members.MemberJpaServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static basic.classroom.dto.LectureResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentLectureJpaService {
    private final MemberJpaServiceV2 memberService;
    private final LectureJpaRepository lectureRepository;
    private final LectureStudentMapperJpaRepository mapperRepository;

    @Transactional
    public Long applyLecture(String loginId, Long lectureId) {
        Student student = findStudent(loginId);
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }

        LectureStudentMapper mapper = LectureStudentMapper.builder().build();
        LectureStudentMapper savedMapper = mapperRepository.save(mapper);
        student.applyLecture(savedMapper, lecture);

        return savedMapper.getId();
    }

    @Transactional
    public void cancelLecture(String loginId, Long mapperId) {
        Student student = findStudent(loginId);
        LectureStudentMapper mapper = student.getMapper(mapperId);
        student.cancelLecture(mapperId);
        mapperRepository.delete(mapper);
    }

    @Transactional(readOnly = true)
    public StudentLectureDetailsResponse getLectureInfo(String loginId, Long mapperId) {
        Student student = findStudent(loginId);
        Lecture lecture = student.getLecture(mapperId);
        StudentLectureDetailsResponse lectureDetailsResponse = StudentLectureDetailsResponse.fromLecture(lecture, mapperId);
        return lectureDetailsResponse;
    }
    @Transactional(readOnly = true)
    public Page<StudentLectureBasicResponse> findMyLectures(String loginId, Long page) {
        Student student = findStudent(loginId);

        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        List<StudentLectureBasicResponse> allLectures = new ArrayList<>();
        Map<Long, LectureStudentMapper> applyingLectures = student.getApplyingLectures();
        applyingLectures.forEach((key, mapper) -> {
            allLectures.add(StudentLectureBasicResponse.fromLecture(mapper.getLecture(), key));
        });
        Page<StudentLectureBasicResponse> lectures = new PageImpl<>(allLectures, pageable, allLectures.size());

        return lectures;
    }

    @Transactional(readOnly = true)
    public Page<StudentFindLecturesResponse> findPersonalizedLectures(String loginId, SearchLectureRequest searchLectureRequest) {
        Student student = findStudent(loginId);

        String status = searchLectureRequest.getStatus();
        String condition = searchLectureRequest.getCondition();
        String text = searchLectureRequest.getText();
        validateSearchCondition(condition, text);

        int page = (searchLectureRequest.getPage() != null) ? searchLectureRequest.getPage().intValue() : 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Map<Long, Long> applyingLectureIds = student.getAllMyLectureIds();

        if (status != null && !status.isBlank() && !status.equals("ALL")) {
            LectureStatus lectureStatus = LectureStatus.valueOf(searchLectureRequest.getStatus());
            if (condition != null && !condition.isBlank()) {
                return findByAllConditions(searchLectureRequest, pageable, applyingLectureIds);
            }
            return findByLectureStatus(lectureStatus, pageable, applyingLectureIds);
        }
        if (condition != null && !condition.isBlank()) {
            return findByCondition(condition, text, pageable, applyingLectureIds);
        }
        return findAllLectures(pageable, applyingLectureIds);
    }
    private void validateSearchCondition(String condition, String text) {
        if (condition != null && !condition.isBlank()) {
            if (text == null || text.isBlank()) {
                throw new LectureSearchParameterException(ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION);
            }
        }
        else if (text != null && !text.isBlank()) {
            if (condition == null || condition.isBlank()) {
                throw new LectureSearchParameterException(ErrorCode.FAILED_FIND_LECTURE_BY_SEARCH_CONDITION);
            }
        }
    }
    private Page<StudentFindLecturesResponse> findAllLectures(Pageable pageable, Map<Long, Long> applyingLectureIds) {
        Page<Lecture> findLectures = lectureRepository.findAll(pageable);
        return getLecturesResponses(applyingLectureIds, findLectures);
    }
    private Page<StudentFindLecturesResponse> findByAllConditions(SearchLectureRequest searchLectureRequest, Pageable pageable, Map<Long, Long> applyingLectureIds) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(searchLectureRequest.getCondition());
        LectureStatus lectureStatus = LectureStatus.valueOf(searchLectureRequest.getStatus());
        String text = searchLectureRequest.getText();

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            Page<Lecture> findLectures = lectureRepository.findByLectureStatusAndInstructor_Member_Name(lectureStatus, text, pageable);
            return getLecturesResponses(applyingLectureIds, findLectures);
        }

        Page<Lecture> findLectures = lectureRepository.findByLectureStatusAndName(lectureStatus, text, pageable);
        return getLecturesResponses(applyingLectureIds, findLectures);
    }
    private Page<StudentFindLecturesResponse> findByLectureStatus(LectureStatus lectureStatus, Pageable pageable, Map<Long, Long> applyingLectureIds) {
        Page<Lecture> findLectures = lectureRepository.findByLectureStatus(lectureStatus, pageable);
        return getLecturesResponses(applyingLectureIds, findLectures);
    }
    private Page<StudentFindLecturesResponse> findByCondition(String condition, String text, Pageable pageable, Map<Long, Long> applyingLectureIds) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            Page<Lecture> findLectures = lectureRepository.findByInstructor_Member_Name(text, pageable);
            return getLecturesResponses(applyingLectureIds, findLectures);
        }

        Page<Lecture> findLectures = lectureRepository.findByName(text, pageable);
        return getLecturesResponses(applyingLectureIds, findLectures);
    }
    private Page<StudentFindLecturesResponse> getLecturesResponses(Map<Long, Long> applyingLectureIds, Page<Lecture> findLectures) {
        return findLectures.map(lecture -> {
            Long mapperId = applyingLectureIds.containsKey(lecture.getId()) ? applyingLectureIds.get(lecture.getId()) : null;
            return StudentFindLecturesResponse.fromLecture(lecture, mapperId);
        });
    }

    private Student findStudent(String loginId) {
        return memberService.findStudentByLoginId(loginId);
    }
}
