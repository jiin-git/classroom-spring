package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import basic.classroom.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class StudentLectureServiceIntegrateTest {

    @Autowired StudentService studentService;
    @Autowired StudentRepository studentRepository;

    @Autowired LectureService lectureService;
    @Autowired LectureRepository lectureRepository;

    @Autowired LectureStudentMapperRepository mapperRepository;

    @Test
    void 강의추가_조회_테스트() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));
        Lecture lecture1 = Lecture.createLecture("lecture", new Instructor(), 10, LectureStatus.OPEN);
        Lecture lecture2 = Lecture.createLecture("lecture2", new Instructor(), 10, LectureStatus.OPEN);

        studentService.join(student);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        // when
        studentService.addLecture(student.getId(), lecture1.getId());
        studentService.addLecture(student.getId(), lecture2.getId());

        List<Lecture> allLectures = studentService.findAllLectures(student.getId());
        List<Student> allStudents = lectureService.findAllStudents(lecture1.getId());

        // then

        assertThat(allLectures).contains(lecture1, lecture2);
        assertThat(allStudents).contains(student);
        assertThat(lecture1.getPersonnel()).isEqualTo(9);

    }

    // test 다시보기
    @Test
    void 강의취소_조회_테스트() {
        // given
        Student student = Student.createStudent(new Member("student", "id", "pw", "email"));
        Lecture lecture1 = Lecture.createLecture("lecture", new Instructor(), 10, LectureStatus.OPEN);
        Lecture lecture2 = Lecture.createLecture("lecture2", new Instructor(), 10, LectureStatus.OPEN);

        studentService.join(student);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        // when
        studentService.addLecture(student.getId(), lecture1.getId());
        studentService.addLecture(student.getId(), lecture2.getId());

        studentService.cancelLecture(student.getId(), lecture1.getId());

        Map<Long, LectureStudentMapper> applyingLectures = student.getApplyingLectures();
        List<Lecture> allLectures = studentService.findAllLectures(student.getId());
        List<Student> allStudents = lectureService.findAllStudents(lecture1.getId());

        // then
       assertThat(allLectures).contains(lecture2);
//        assertThat(allStudents).contains(student);
        assertThat(allLectures.size()).isEqualTo(1);
        assertThat(allStudents.size()).isEqualTo(0);
        assertThat(lecture1.getPersonnel()).isEqualTo(10);
        assertThat(lecture2.getPersonnel()).isEqualTo(9);

    }

}
