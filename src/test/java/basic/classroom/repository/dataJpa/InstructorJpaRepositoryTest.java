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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InstructorJpaRepositoryTest {
    @Autowired InstructorJpaRepository instructorJpaRepository;
    @Autowired LectureJpaRepository lectureJpaRepository;

    @Test
    void findByMember_LoginId() {
        Instructor instructorA = new Instructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = new Instructor(new Member("instructorB", "instIdB", "pw", "email"));

        Instructor savedInstructorA = instructorJpaRepository.save(instructorA);
        Instructor savedInstructorB = instructorJpaRepository.save(instructorB);

        Optional<Instructor> findByLoginIdA = instructorJpaRepository.findByMember_LoginId(savedInstructorA.getMember().getLoginId());
        Optional<Instructor> findByLoginIdB = instructorJpaRepository.findByMember_LoginId(savedInstructorB.getMember().getLoginId());

        assertThat(findByLoginIdA.get()).isEqualTo(instructorA);
        assertThat(findByLoginIdB.get()).isEqualTo(instructorB);
    }

    @Test
    void findByMember_Name() {
        Instructor instructorA = new Instructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = new Instructor(new Member("instructorB", "instIdB", "pw", "email"));

        Instructor savedInstructorA = instructorJpaRepository.save(instructorA);
        Instructor savedInstructorB = instructorJpaRepository.save(instructorB);

        List<Instructor> findByNameA = instructorJpaRepository.findByMember_Name(savedInstructorA.getMember().getName());
        List<Instructor> findByNameB = instructorJpaRepository.findByMember_Name(savedInstructorB.getMember().getName());

        assertThat(findByNameA).contains(instructorA);
        assertThat(findByNameB).contains(instructorB);
    }

    @Test
    void findLecturesById() {
        Instructor instructorA = new Instructor(new Member("instructorA", "instIdA", "pw", "email"));
        Instructor instructorB = new Instructor(new Member("instructorB", "instIdB", "pw", "email"));

        Instructor savedInstructorA = instructorJpaRepository.save(instructorA);
        Instructor savedInstructorB = instructorJpaRepository.save(instructorB);

        Lecture lectureA = Lecture.createLecture("Spring JPA", instructorA, 10, LectureStatus.OPEN);
        Lecture lectureB = Lecture.createLecture("Spring Data JPA", instructorA, 10, LectureStatus.OPEN);
        Lecture lectureC = Lecture.createLecture("Spring MVC", instructorB, 10, LectureStatus.OPEN);
        Lecture lectureD = Lecture.createLecture("Spring Security", instructorA, 10, LectureStatus.OPEN);

        lectureJpaRepository.save(lectureA);
        lectureJpaRepository.save(lectureB);
        lectureJpaRepository.save(lectureC);
        lectureJpaRepository.save(lectureD);

        Pageable firstPage = PageRequest.of(0, 2);
        Pageable secondPage = PageRequest.of(1, 2);

        Page<Lecture> lecturesByAIdAndFirstPage = instructorJpaRepository.findLecturesById(instructorA.getId(), firstPage);
        Page<Lecture> lecturesByAIdAndSecondPage = instructorJpaRepository.findLecturesById(instructorA.getId(), secondPage);
        Page<Lecture> lecturesByBIdAndFirstPage = instructorJpaRepository.findLecturesById(instructorB.getId(), firstPage);
        Page<Lecture> lecturesByBIdAndSecondPage = instructorJpaRepository.findLecturesById(instructorB.getId(), secondPage);

        assertThat(lecturesByAIdAndFirstPage.getTotalElements()).isEqualTo(3);
        assertThat(lecturesByAIdAndSecondPage.getTotalElements()).isEqualTo(3);
//        assertThat(lecturesByBIdAndFirstPage.getTotalElements()).isEqualTo(1);

        assertThat(lecturesByAIdAndFirstPage.getTotalPages()).isEqualTo(2);
        assertThat(lecturesByAIdAndSecondPage.getTotalPages()).isEqualTo(2);
//        assertThat(lecturesByBIdAndFirstPage.getTotalPages()).isEqualTo(1);

        assertThat(lecturesByAIdAndFirstPage.getNumberOfElements()).isEqualTo(2);
        assertThat(lecturesByAIdAndSecondPage.getNumberOfElements()).isEqualTo(1);
//        assertThat(lecturesByBIdAndFirstPage.getNumberOfElements()).isEqualTo(1);
//        assertThat(lecturesByBIdAndSecondPage.getNumberOfElements()).isEqualTo(0);

    }
}