package basic.classroom.service;

import basic.classroom.dto.LoginDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.domain.*;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import basic.classroom.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final LectureStudentMapperRepository mapperRepository;

    @Transactional
    public Long join(Student student) {
        String studentLoginId = student.getMember().getLoginId();
        Optional<Student> duplicateStudent = studentRepository.findByLoginId(studentLoginId);

        // 중복 회원 존재시 에러
        if (duplicateStudent.isPresent()) {
            log.info("duplicated!");
            throw new IllegalStateException();
        }

        // 성공 로직
        studentRepository.save(student);

        return student.getId();
    }

    public Student findOne(Long id) {
        return studentRepository.findOne(id);
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Student student = studentRepository.findOne(id);
        student.getMember().setEmail(updateMemberDto.getEmail());
    }

    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

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
