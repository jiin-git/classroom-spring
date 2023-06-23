package basic.classroom.repository;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.Member;
import basic.classroom.repository.InstructorRepository;
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
}