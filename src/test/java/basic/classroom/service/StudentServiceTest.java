package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import basic.classroom.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentServiceTest {

    @Autowired StudentService studentService;
    @Autowired StudentRepository studentRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired LectureStudentMapperRepository mapperRepository;

    @Test
    void 회원가입() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));

        // when
        Long id = studentService.join(student);

        // then
        Student findStudent = studentRepository.findOne(id);
        assertThat(findStudent).isEqualTo(student);
    }

    @Test
    void 이름조회() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));

        // when
        Long id = studentService.join(student);

        // then
        Student findStudent = studentRepository.findOne(id);
        List<Student> findByNameStudents = studentService.findByName("student");
        assertThat(findByNameStudents).contains(findStudent);
        assertThat(findByNameStudents.size()).isEqualTo(1);
    }

    @Test
    void 수강신청() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));
        Lecture lecture = Lecture.createLecture("lecture", new Instructor(), 10, LectureStatus.READY);

        Long studentId = studentService.join(student);
        lectureRepository.save(lecture);
        Long lectureId = lecture.getId();

        // when
        studentService.addLecture(studentId, lectureId);

        // then
        Map<Long, LectureStudentMapper> applyingLectures = student.getApplyingLectures();
        Map<Long, LectureStudentMapper> appliedStudents = lecture.getAppliedStudents();
        assertThat(student).isEqualTo(applyingLectures.get(studentId).getStudent());
        assertThat(lecture).isEqualTo(appliedStudents.get(lectureId).getLecture());

        assertThat(student.getApplyingLectures().size()).isEqualTo(1);
        assertThat(lecture.getAppliedStudents().size()).isEqualTo(1);

    }

    @Test
    void 수강신청취소() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));
        Lecture lecture = Lecture.createLecture("lecture", new Instructor(), 10, LectureStatus.READY);

        Long studentId = studentService.join(student);
        lectureRepository.save(lecture);
        Long lectureId = lecture.getId();

        // when
        studentService.addLecture(studentId, lectureId);
        Long mapperId = student.getApplyingLectures().get(lectureId).getId();
        LectureStudentMapper savedMapper = mapperRepository.findOne(mapperId);
        studentService.cancelLecture(studentId, lectureId);

        // then
        LectureStudentMapper removedMapper = mapperRepository.findOne(mapperId);
//        assertThatThrownBy(() -> mapperRepository.findOne(mapperId)).isInstanceOf(NotFound.class);
        assertThat(removedMapper).isEqualTo(null);
        assertThat(student.getApplyingLectures().size()).isEqualTo(0);
        assertThat(lecture.getAppliedStudents().size()).isEqualTo(0);

    }
}