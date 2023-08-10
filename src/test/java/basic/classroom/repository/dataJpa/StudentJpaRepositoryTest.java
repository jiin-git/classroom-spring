package basic.classroom.repository.dataJpa;

import basic.classroom.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentJpaRepositoryTest {

    @Autowired StudentJpaRepository studentJpaRepository;
    @Autowired InstructorJpaRepository instructorJpaRepository;
    @Autowired LectureJpaRepository lectureJpaRepository;
    @Autowired LectureStudentMapperJpaRepository mapperJpaRepository;

    @Test
    void findByMember_LoginId() {
        Student studentA = Student.createStudent(new Member("studentA", "stuIdA", "pw", "email"));
        Student studentB = Student.createStudent(new Member("studentA", "stuIdB", "pw", "email"));

        Student savedStudentA = studentJpaRepository.save(studentA);
        Student savedStudentB = studentJpaRepository.save(studentB);

        Optional<Student> findByLoginIdA = studentJpaRepository.findByMember_LoginId(savedStudentA.getMember().getLoginId());
        Optional<Student> findByLoginIdB = studentJpaRepository.findByMember_LoginId(savedStudentB.getMember().getLoginId());

        assertThat(findByLoginIdA.get()).isEqualTo(studentA);
        assertThat(findByLoginIdB.get()).isEqualTo(studentB);
    }

    @Test
    void findByMember_Name() {
        Student studentA = Student.createStudent(new Member("studentA", "stuIdA", "pw", "email"));
        Student studentB = Student.createStudent(new Member("studentA", "stuIdB", "pw", "email"));

        Student savedStudentA = studentJpaRepository.save(studentA);
        Student savedStudentB = studentJpaRepository.save(studentB);

        List<Student> findByLoginIdA = studentJpaRepository.findByMember_Name(savedStudentA.getMember().getName());
        List<Student> findByLoginIdB = studentJpaRepository.findByMember_Name(savedStudentB.getMember().getName());

        assertThat(findByLoginIdA).contains(studentA);
        assertThat(findByLoginIdB).contains(studentB);
    }

    // integrity test
    @Test
    void findLecturesByApplyingLectures_Student_Id() {
        Student studentA = Student.createStudent(new Member("studentA", "stuIdA", "pw", "email"));
        Student studentB = Student.createStudent(new Member("studentA", "stuIdB", "pw", "email"));
        studentJpaRepository.save(studentA);
        studentJpaRepository.save(studentB);

        Instructor instructorA = Instructor.createInstructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = Instructor.createInstructor(new Member("instructorB", "instIdB", "pw", "email"));
        instructorJpaRepository.save(instructorA);
        instructorJpaRepository.save(instructorB);

        Lecture lectureA = Lecture.createLecture("lectureA", instructorA, 10, LectureStatus.OPEN);
        Lecture lectureB = Lecture.createLecture("lectureB", instructorA, 10, LectureStatus.OPEN);
        Lecture lectureC = Lecture.createLecture("lectureC", instructorB, 10, LectureStatus.OPEN);
        lectureJpaRepository.save(lectureA);
        lectureJpaRepository.save(lectureB);
        lectureJpaRepository.save(lectureC);

        LectureStudentMapper lsMapperA = new LectureStudentMapper();
        mapperJpaRepository.save(lsMapperA);
        studentA.applyLecture(lsMapperA, lectureA);

        LectureStudentMapper lsMapperB = new LectureStudentMapper();
        mapperJpaRepository.save(lsMapperB);
        studentA.applyLecture(lsMapperB, lectureB);

        LectureStudentMapper lsMapperC = new LectureStudentMapper();
        mapperJpaRepository.save(lsMapperC);
        studentA.applyLecture(lsMapperC, lectureC);

        LectureStudentMapper lsMapperD = new LectureStudentMapper();
        mapperJpaRepository.save(lsMapperD);
        studentB.applyLecture(lsMapperD, lectureC);

        Pageable firstPage = PageRequest.of(0, 2);
        Pageable secondPage = PageRequest.of(1, 2);

        Page<Lecture> findLecturesByAId = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(studentA.getId(), firstPage);
        Page<Lecture> findLecturesByAId2 = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(studentA.getId(), secondPage);
        Page<Lecture> findLecturesByBId = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(studentB.getId(), firstPage);

        assertThat(findLecturesByAId.getTotalElements()).isEqualTo(3);
        assertThat(findLecturesByAId2.getTotalElements()).isEqualTo(3);
        assertThat(findLecturesByBId.getTotalElements()).isEqualTo(1);

        assertThat(findLecturesByAId.getNumberOfElements()).isEqualTo(2);
        assertThat(findLecturesByAId2.getNumberOfElements()).isEqualTo(1);
        assertThat(findLecturesByBId.getNumberOfElements()).isEqualTo(1);

        assertThat(findLecturesByAId.getContent()).contains(lectureA, lectureB);
        assertThat(findLecturesByAId2.getContent()).contains(lectureC);
        assertThat(findLecturesByBId.getContent()).contains(lectureC);

        assertThat(findLecturesByAId.isFirst()).isTrue();
        assertThat(findLecturesByAId2.isLast()).isTrue();
        assertThat(findLecturesByBId.isFirst()).isTrue();
        assertThat(findLecturesByBId.isLast()).isTrue();
    }
}