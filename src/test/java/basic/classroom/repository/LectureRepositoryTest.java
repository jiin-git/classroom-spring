package basic.classroom.repository;

import basic.classroom.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    void pagination() {
        Instructor instructor = Instructor.createInstructor(new Member("instructor1", "loginId", "pw", "email"));

        instructorRepository.save(instructor);

        Lecture lecture2 = Lecture.createLecture("lecture2", instructor, 10, LectureStatus.READY);
        Lecture lecture3 = Lecture.createLecture("lecture3", instructor, 10, LectureStatus.READY);
        Lecture lecture4 = Lecture.createLecture("lecture4", instructor, 10, LectureStatus.READY);

        for (int i = 0; i < 10; i++) {
            Lecture lecture1 = Lecture.createLecture("lecture1", instructor, 10, LectureStatus.READY);
            lectureRepository.save(lecture1);
        }
        lectureRepository.save(lecture2);
        lectureRepository.save(lecture3);
        lectureRepository.save(lecture4);

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> allLectures = lectureRepository.findAll();
        List<Lecture> findByPageOneLectures = lectureRepository.findByPage(pageOne, pageSize);
        List<Lecture> findByPageTwoLectures = lectureRepository.findByPage(pageTwo, pageSize);

        assertThat(allLectures.size()).isEqualTo(13);
        assertThat(findByPageOneLectures.size()).isEqualTo(10);
//        assertThat(findByPageOneLectures).contains(lecture1);
        assertThat(findByPageTwoLectures.size()).isEqualTo(3);
        assertThat(findByPageTwoLectures).contains(lecture2, lecture3, lecture4);
    }

    @Test
    void findByPageByLectureStatusTest() {
        Instructor instructorA = Instructor.createInstructor(new Member("instructorA", "loginId", "pw", "email"));
        Instructor instructorB = Instructor.createInstructor(new Member("instructorB", "loginId", "pw", "email"));
        instructorRepository.save(instructorA);
        instructorRepository.save(instructorB);

        Lecture lecture2 = Lecture.createLecture("lecture2", instructorB, 10, LectureStatus.READY);
        Lecture lecture3 = Lecture.createLecture("lecture3", instructorB, 10, LectureStatus.OPEN);
        Lecture lecture4 = Lecture.createLecture("lecture4", instructorB, 10, LectureStatus.FULL);

        for (int i = 0; i < 10; i++) {
            Lecture lecture1 = Lecture.createLecture("lecture1", instructorA, 10, LectureStatus.READY);
            lectureRepository.save(lecture1);
        }
        lectureRepository.save(lecture2);
        lectureRepository.save(lecture3);
        lectureRepository.save(lecture4);

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> allLectures = lectureRepository.findAll();
        List<Lecture> byPageByLectureStatusReady = lectureRepository.findByPageByLectureStatus(pageTwo, pageSize, LectureStatus.READY);
        List<Lecture> byPageByLectureStatusOpen = lectureRepository.findByPageByLectureStatus(pageOne, pageSize, LectureStatus.OPEN);
        List<Lecture> byPageByLectureStatusFull = lectureRepository.findByPageByLectureStatus(pageOne, pageSize, LectureStatus.FULL);

        assertThat(allLectures.size()).isEqualTo(13);
        assertThat(byPageByLectureStatusReady.size()).isEqualTo(1);
        assertThat(byPageByLectureStatusOpen.size()).isEqualTo(1);
        assertThat(byPageByLectureStatusOpen).contains(lecture3);
        assertThat(byPageByLectureStatusFull.size()).isEqualTo(1);
        assertThat(byPageByLectureStatusFull).contains(lecture4);
    }
    @Test
    void 단일_조건_조회_테스트() {
        Instructor instructorA = Instructor.createInstructor(new Member("instructorA", "loginId", "pw", "email"));
        instructorRepository.save(instructorA);

        Lecture lecture1 = Lecture.createLecture("lecture1", instructorA, 10, LectureStatus.READY);
        Lecture lecture2 = Lecture.createLecture("lecture2", instructorA, 10, LectureStatus.OPEN);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        for (int i = 0; i < 10; i++) {
            Lecture lecture = Lecture.createLecture("lecture1", instructorA, 10, LectureStatus.READY);
            lectureRepository.save(lecture);
        }

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> allLectures = lectureRepository.findAll();
        List<Lecture> findByPageFirstByNameLecture1 = lectureRepository.findByPageByName(pageOne, pageSize, lecture1.getName());
        List<Lecture> findByPageSecondByNameLecture1 = lectureRepository.findByPageByName(pageTwo, pageSize, lecture1.getName());
        List<Lecture> findByPageByNameLecture2 = lectureRepository.findByPageByName(pageOne, pageSize, lecture2.getName());
        List<Lecture> findByFirstPageByInstructorName = lectureRepository.findByPageByInstructorName(pageOne, pageSize, instructorA.getMember().getName());
        List<Lecture> findBySecondPageByInstructorName = lectureRepository.findByPageByInstructorName(pageTwo, pageSize, instructorA.getMember().getName());

        assertThat(allLectures.size()).isEqualTo(12);
        assertThat(findByPageFirstByNameLecture1.size()).isEqualTo(10);
        assertThat(findByPageSecondByNameLecture1.size()).isEqualTo(1);
        assertThat(findByPageByNameLecture2.size()).isEqualTo(1);
        assertThat(findByFirstPageByInstructorName.size()).isEqualTo(10);
        assertThat(findBySecondPageByInstructorName.size()).isEqualTo(2);
    }
    @Test
    void 복합_조건_조회_테스트() {
        Instructor instructorA = Instructor.createInstructor(new Member("instructorA", "loginId", "pw", "email"));
        Instructor instructorB = Instructor.createInstructor(new Member("instructorB", "loginId", "pw", "email"));
        instructorRepository.save(instructorA);
        instructorRepository.save(instructorB);

        Lecture lecture1 = Lecture.createLecture("lecture1", instructorB, 10, LectureStatus.READY);
        Lecture lecture2 = Lecture.createLecture("lecture2", instructorB, 10, LectureStatus.OPEN);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        for (int i = 0; i < 11; i++) {
            Lecture lecture = Lecture.createLecture("lecture1", instructorA, 10, LectureStatus.FULL);
            lectureRepository.save(lecture);
        }

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> allLectures = lectureRepository.findAll(); // 13
        List<Lecture> findByReadyAndLectureName = lectureRepository.findByPageByLectureStatusByName(pageOne, pageSize, LectureStatus.READY, lecture1.getName()); // 1
        List<Lecture> findByNull = lectureRepository.findByPageByLectureStatusByName(pageOne, pageSize, LectureStatus.READY, lecture2.getName()); // 0
        List<Lecture> findByPageAndFullAndInstructorName = lectureRepository.findByPageByLectureStatusByInstructorName(pageOne, pageSize, LectureStatus.FULL, instructorA.getMember().getName()); // 10
        List<Lecture> findBySecondPageAndFullAndInstructorName = lectureRepository.findByPageByLectureStatusByInstructorName(pageTwo, pageSize, LectureStatus.FULL, instructorA.getMember().getName()); // 1
        List<Lecture> findByNullTwo = lectureRepository.findByPageByLectureStatusByInstructorName(pageTwo, pageSize, LectureStatus.FULL, instructorB.getMember().getName()); // 0

        assertThat(allLectures.size()).isEqualTo(13);
        assertThat(findByReadyAndLectureName.size()).isEqualTo(1);
        assertThat(findByNull).isEmpty();
        assertThat(findByPageAndFullAndInstructorName.size()).isEqualTo(10);
        assertThat(findBySecondPageAndFullAndInstructorName.size()).isEqualTo(1);
        assertThat(findByNullTwo).isEmpty();
    }
}