package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import basic.classroom.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static basic.classroom.service.ProfileImageService.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final LectureStudentMapperRepository mapperRepository;

//    ===== lecture service =====
    @Transactional
    public Long addLecture(Long studentId, Long lectureId) {
        // 엔티티 조회
        Student student = studentRepository.findOne(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        // mapper 생성
        LectureStudentMapper mapper = new LectureStudentMapper();
        mapper.setStudent(student);
        mapper.setLecture(lecture);

        // mapper 저장 및 설정
        mapperRepository.save(mapper);

        student.applyLecture(mapper);
        lecture.addStudent(mapper);

        return lectureId;
    }

    @Transactional
    public void cancelLecture(Long studentId, Long lectureId) {
        // 엔티티 조회
        Student student = studentRepository.findOne(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        // mapper 제거
        LectureStudentMapper mapper = student.getApplyingLectures().get(lectureId);
        mapperRepository.cancel(mapper);

        student.cancelLecture(lectureId);
        lecture.removeStudent(studentId);
    }

    public List<Lecture> findAllLectures(Long id) {
        Student student = studentRepository.findOne(id);
        List<Lecture> lectures = student.findAllLectures();

        return lectures;
    }
}
