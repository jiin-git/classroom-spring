package basic.classroom.repository;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.Member;
import basic.classroom.repository.jpa.InstructorRepository;
import basic.classroom.repository.jpa.LectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InstructorRepositoryTest {
    @Autowired InstructorRepository instructorRepository;
    @Autowired LectureRepository lectureRepository;

    @Test
    void create_test() throws Exception {
        //given
        Instructor instructor = new Instructor();
        instructor.setMember(new Member("test", "testId", "pw", "email@email.com"));
        instructorRepository.save(instructor);

        Lecture lecture1 = new Lecture();
        lecture1.setName("Spring Boot Lecture");

        //when
        Instructor findInstructor = instructorRepository.findOne(instructor.getId());
//        List<Instructor> findByLoginID = instructorRepository.findByLoginID(instructor.getMember().getLoginId());
        findInstructor.addLectures(lecture1);

        //then
        List<Lecture> lectures = findInstructor.getLectures().values().stream().toList();
        assertThat(findInstructor).isEqualTo(instructor);
//        assertThat(findByLoginID).contains(instructor);
        assertThat(lectures.size()).isEqualTo(1);
        assertThat(lectures).contains(lecture1);
    }

    @Test
    void findByPageMyLecturesTest() {
        Instructor instructor = Instructor.createInstructor(new Member("instructor1", "loginId", "pw", "email"));
        instructorRepository.save(instructor);

        Lecture lecture2 = Lecture.createLecture("lecture2", instructor, 10, LectureStatus.READY);
        Lecture lecture3 = Lecture.createLecture("lecture3", instructor, 10, LectureStatus.READY);
        Lecture lecture4 = Lecture.createLecture("lecture4", instructor, 10, LectureStatus.READY);

        for (int i = 0; i < 10; i++) {
            Lecture lecture1 = Lecture.createLecture("lecture1", instructor, 10, LectureStatus.READY);
            lectureRepository.save(lecture1);
            instructor.addLectures(lecture1);
        }
        lectureRepository.save(lecture2);
        lectureRepository.save(lecture3);
        lectureRepository.save(lecture4);
        instructor.addLectures(lecture2);
        instructor.addLectures(lecture3);
        instructor.addLectures(lecture4);

        int pageOne = 1;
        int pageTwo = 2;
        int pageSize = 10;

        List<Lecture> lectures = instructor.getLectures().values().stream().toList();
        List<Lecture> findByPageOneLectures = instructorRepository.findByPageMyLectures(instructor.getId(), pageOne, pageSize);
        List<Lecture> findByPageTwoLectures = instructorRepository.findByPageMyLectures(instructor.getId(), pageTwo, pageSize);

        assertThat(lectures.size()).isEqualTo(13);
        assertThat(findByPageOneLectures.size()).isEqualTo(10);
//        assertThat(findByPageOneLectures).contains(lecture1);
        assertThat(findByPageTwoLectures.size()).isEqualTo(3);
        assertThat(findByPageTwoLectures).contains(lecture2, lecture3, lecture4);
    }
}