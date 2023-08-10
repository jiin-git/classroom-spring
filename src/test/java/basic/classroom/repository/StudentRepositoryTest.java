package basic.classroom.repository;

import basic.classroom.domain.*;
import basic.classroom.repository.jpa.InstructorRepository;
import basic.classroom.repository.jpa.LectureRepository;
import basic.classroom.repository.jpa.LectureStudentMapperRepository;
import basic.classroom.repository.jpa.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudentRepositoryTest {

    @Autowired StudentRepository studentRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired LectureStudentMapperRepository mapperRepository;

    @Test
    void findByPageMyLectures() {
        Instructor instructor = Instructor.createInstructor(new Member("instructor1", "loginId", "pw", "email"));
        Student student = Student.createStudent(new Member("student", "studentId", "pw", "email"));
        instructorRepository.save(instructor);
        studentRepository.save(student);

        Lecture lecture2 = Lecture.createLecture("lecture2", instructor, 10, LectureStatus.READY);
        lectureRepository.save(lecture2);

        for (int i = 0; i < 10; i++) {
            Lecture lecture1 = Lecture.createLecture("lecture1", instructor, 10, LectureStatus.READY);
            lectureRepository.save(lecture1);

            LectureStudentMapper mapper = new LectureStudentMapper();;
            mapperRepository.save(mapper);
            student.applyLecture(mapper, lecture1);
        }

        LectureStudentMapper mapper2 = new LectureStudentMapper();
        mapperRepository.save(mapper2);
        student.applyLecture(mapper2, lecture2);

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> lectures = student.getApplyingLectures().values().stream().map(al -> al.getLecture()).toList();
        List<Lecture> findByPageOneLectures = studentRepository.findByPageMyLectures(student.getId(), pageOne, pageSize);
        List<Lecture> findByPageTwoLectures = studentRepository.findByPageMyLectures(student.getId(), pageTwo, pageSize);

        assertThat(lectures.size()).isEqualTo(11);
        assertThat(findByPageOneLectures.size()).isEqualTo(10);
//        assertThat(findByPageOneLectures).contains(lecture1);
        assertThat(findByPageTwoLectures.size()).isEqualTo(1);
        assertThat(findByPageTwoLectures).contains(lecture2);
    }
}