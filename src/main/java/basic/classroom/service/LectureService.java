package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureStudentMapperRepository mapperRepository;

    @Transactional
    public Long create(Lecture lecture) {
        lectureRepository.save(lecture);
        return lecture.getId();
    }

    public Lecture findOne(Long id){
        return lectureRepository.findOne(id);
    }

    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public List<Lecture> findPersonalizedLectures(SearchConditionDto searchConditionDto) {
        String status = searchConditionDto.getStatus();
        String condition = searchConditionDto.getCondition();
        String text = searchConditionDto.getText();

        if (status != null && !status.isBlank() && !status.equals("ALL")) {
            List<Lecture> findByLectureStatusList = findByLectureStatus(status);

            if (condition != null && !condition.isBlank()) {
                return findByAllConditions(condition, text, findByLectureStatusList);
            }

            return findByLectureStatusList;
        }

        if (condition != null && !condition.isBlank()) {
            return findByCondition(condition, text);
        }

        List<Lecture> lectures = findAll();
        return lectures;
    }

    private static List<Lecture> findByAllConditions(String condition, String text, List<Lecture> findByLectureStatusList) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            List<Lecture> filterInstructor = findByLectureStatusList.stream()
                    .filter(l -> l.getInstructor().getMember().getName().equals(text)).toList();
            return filterInstructor;
        }

        List<Lecture> filterLectureName = findByLectureStatusList.stream()
                .filter(l -> l.getName().equals(text)).toList();
        return filterLectureName;
    }

    private List<Lecture> findByLectureStatus(String status) {
        LectureStatus lectureStatus = LectureStatus.valueOf(status);
        List<Lecture> findByLectureStatusList = findByLectureStatus(lectureStatus);
        return findByLectureStatusList;
    }

    private List<Lecture> findByCondition(String condition, String text) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);
        
        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            List<Lecture> findByInstructorList = findByInstructorName(text);
            return findByInstructorList;
        }

        List<Lecture> findByLectureList = findByName(text);
        return findByLectureList;
    }
    
    public List<Lecture> findByName(String name) {
        return lectureRepository.findByName(name);
    }

    public List<Lecture> findByInstructorName(String instructorName) {
        return lectureRepository.findByInstructorName(instructorName);
    }

    public List<Lecture> findByLectureStatus(LectureStatus lectureStatus) {
        return lectureRepository.findByLectureStatus(lectureStatus);
    }

    public List<Student> findAllStudents(Long id) {
        Lecture lecture = lectureRepository.findOne(id);
        List<Student> students = lecture.findAppliedStudents();

        return students;
    }

}
