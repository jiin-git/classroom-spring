package basic.classroom.repository.dataJpa;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LectureJpaRepositoryTest {
    @Autowired LectureJpaRepository lectureJpaRepository;
    @Autowired InstructorJpaRepository instructorJpaRepository;

    @Test
    void findByName() {
        Instructor instructorA = new Instructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = new Instructor(new Member("instructorB", "instIdA", "pw", "email"));

        instructorJpaRepository.save(instructorA);
        instructorJpaRepository.save(instructorB);

        Lecture lectureA = Lecture.createLecture("lectureA", instructorA, 10, LectureStatus.READY);
        Lecture lectureA2 = Lecture.createLecture("lectureA", instructorA, 10, LectureStatus.READY);
        Lecture lectureA3 = Lecture.createLecture("lectureA", instructorA, 10, LectureStatus.READY);
        Lecture lectureB = Lecture.createLecture("lectureB", instructorA, 10, LectureStatus.OPEN);

        Lecture savedLectureA = lectureJpaRepository.save(lectureA);
        Lecture savedLectureB = lectureJpaRepository.save(lectureB);
        lectureJpaRepository.save(lectureA2);
        lectureJpaRepository.save(lectureA3);

        Pageable firstPage = PageRequest.of(0, 2);
        Pageable secondPage = PageRequest.of(1, 2);

        List<Lecture> findLectureA = lectureJpaRepository.findByName(savedLectureA.getName());
        List<Lecture> findLectureB = lectureJpaRepository.findByName(savedLectureB.getName());
        Page<Lecture> findLectureAByFirstPage = lectureJpaRepository.findByName(savedLectureA.getName(), firstPage);
        Page<Lecture> findLectureABySecondPage = lectureJpaRepository.findByName(savedLectureA.getName(), secondPage);

        assertThat(findLectureA).contains(lectureA);
        assertThat(findLectureB).contains(lectureB);
        assertThat(findLectureAByFirstPage.getContent()).contains(lectureA);
        assertThat(findLectureAByFirstPage.getTotalElements()).isEqualTo(3);
        assertThat(findLectureABySecondPage.getTotalElements()).isEqualTo(3);
        assertThat(findLectureAByFirstPage.getNumberOfElements()).isEqualTo(2);
        assertThat(findLectureABySecondPage.getNumberOfElements()).isEqualTo(1);
        assertThat(findLectureAByFirstPage.isFirst()).isTrue();
        assertThat(findLectureABySecondPage.isLast()).isTrue();
    }

    @Test
    void findTwoConditionTest() {
        Instructor instructorA = new Instructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = new Instructor(new Member("instructorB", "instIdA", "pw", "email"));

        instructorJpaRepository.save(instructorA);
        instructorJpaRepository.save(instructorB);

        Lecture lectureA = Lecture.createLecture("lectureA", instructorA, 10, LectureStatus.READY);
        Lecture lectureA2 = Lecture.createLecture("lectureA", instructorB, 10, LectureStatus.READY);
        Lecture lectureB = Lecture.createLecture("lectureB", instructorA, 10, LectureStatus.OPEN);
        Lecture lectureC = Lecture.createLecture("lectureC", instructorB, 10, LectureStatus.READY);

        lectureJpaRepository.save(lectureA);
        lectureJpaRepository.save(lectureA2);
        lectureJpaRepository.save(lectureB);
        lectureJpaRepository.save(lectureC);

        Pageable pageable = PageRequest.of(0, 1);
        Pageable pageable2 = PageRequest.of(1, 1);

        List<Lecture> findLectureA = lectureJpaRepository.findByLectureStatusAndName(LectureStatus.READY, lectureA.getName());
        List<Lecture> findLectureB = lectureJpaRepository.findByLectureStatusAndInstructor_Member_Name(LectureStatus.READY, instructorB.getMember().getName());
        Page<Lecture> findLectureByPage = lectureJpaRepository.findByLectureStatusAndInstructor_Member_Name(LectureStatus.READY, instructorB.getMember().getName(), pageable);
        Page<Lecture> findLectureByPage2 = lectureJpaRepository.findByLectureStatusAndInstructor_Member_Name(LectureStatus.READY, instructorB.getMember().getName(), pageable2);

        assertThat(findLectureA).contains(lectureA, lectureA2);
        assertThat(findLectureB).contains(lectureA2, lectureC);
        assertThat(findLectureA.size()).isEqualTo(2);
        assertThat(findLectureB.size()).isEqualTo(2);

        assertThat(findLectureByPage.getTotalElements()).isEqualTo(2);
        assertThat(findLectureByPage2.getTotalElements()).isEqualTo(2);
        assertThat(findLectureByPage.getNumberOfElements()).isEqualTo(1);
        assertThat(findLectureByPage2.getNumberOfElements()).isEqualTo(1);
        assertThat(findLectureByPage.getContent()).contains(lectureA2);
        assertThat(findLectureByPage2.getContent()).contains(lectureC);

    }
}