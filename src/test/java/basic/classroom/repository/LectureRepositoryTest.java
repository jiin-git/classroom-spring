package basic.classroom.repository;

import basic.classroom.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LectureRepositoryTest {

    @Autowired LectureRepository lectureRepository;
    @Autowired InstructorRepository instructorRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired LectureStudentMapperRepository lectureStudentMapperRepository;

    @Test
    void save() {
        // given
        Instructor instructor = new Instructor();
        instructor.setMember(new Member("instructor", "instructor_id", "pw", "email"));
        instructorRepository.save(instructor);

        Student student = new Student();
        student.setMember(new Member("student", "student_id", "pw", "email"));
        studentRepository.save(student);

        Lecture lecture = new Lecture();
        lecture.setName("lecture");
        lecture.setPersonnel(2);
        lectureRepository.save(lecture);

        //when
        Instructor findInstructor = instructorRepository.findOne(instructor.getId());
        findInstructor.addLectures(lecture);

        Student findStudent = studentRepository.findOne(student.getId());
        Lecture findLecture = lectureRepository.findOne(lecture.getId());

        LectureStudentMapper ls_mapper = new LectureStudentMapper();
        ls_mapper.setLecture(findLecture);
        ls_mapper.setStudent(findStudent);
        lectureStudentMapperRepository.save(ls_mapper);


        //then
        assertThat(findLecture.getInstructor()).isEqualTo(instructor);
    }

}