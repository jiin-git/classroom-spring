package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.repository.InstructorRepository;
import basic.classroom.repository.LectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LectureServiceTest {

    @Autowired LectureService lectureService;
    @Autowired LectureRepository lectureRepository;
    @Autowired InstructorService instructorService;
    @Autowired InstructorRepository instructorRepository;

    @Test
    void 조건별_조회_테스트() {
        //given
        AddLectureDto lectureDto1 = new AddLectureDto("lecture1", 10, LectureStatus.READY);
        AddLectureDto lectureDto2 = new AddLectureDto("lecture2", 10, LectureStatus.OPEN);
        AddLectureDto lectureDto3 = new AddLectureDto("lecture3", 10, LectureStatus.FULL);

        Instructor instructor = Instructor.createInstructor(new Member("instructor", "id", "pw", "email"));
        instructorService.join(instructor);

        //when
        Long lecture1Id = instructorService.addLecture(instructor.getId(), lectureDto1);
        Long lecture2Id = instructorService.addLecture(instructor.getId(), lectureDto2);
        Long lecture3Id = instructorService.addLecture(instructor.getId(), lectureDto3);

        Lecture lecture1 = lectureRepository.findOne(lecture1Id);
        Lecture lecture2 = lectureRepository.findOne(lecture2Id);
        Lecture lecture3 = lectureRepository.findOne(lecture3Id);

        //then

        assertThat(lecture1.getName()).isEqualTo("lecture1");
        assertThat(lecture1.getInstructor()).isEqualTo(instructor);
        assertThat(lecture1.getPersonnel()).isEqualTo(10);
        assertThat(lecture1.getLectureStatus()).isEqualTo(LectureStatus.READY);

        List<Lecture> lectures = lectureService.findAll();
        assertThat(lectures.size()).isEqualTo(3);
        assertThat(lectures).contains(lecture1, lecture2, lecture3);

    }

    @Test
    void 이름기반_조회() {
        //given
        AddLectureDto lectureDto1 = new AddLectureDto("lecture1", 10, LectureStatus.READY);
        AddLectureDto lectureDto2 = new AddLectureDto("lecture2", 10, LectureStatus.OPEN);
        AddLectureDto lectureDto3 = new AddLectureDto("lecture3", 10, LectureStatus.FULL);

        Instructor instructor = Instructor.createInstructor(new Member("instructor", "id", "pw", "email"));
        instructorService.join(instructor);

        //when
        Long lectureId = instructorService.addLecture(instructor.getId(), lectureDto1);
        Lecture savedLecture = lectureRepository.findOne(lectureId);

        instructorService.addLecture(instructor.getId(), lectureDto2);
        instructorService.addLecture(instructor.getId(), lectureDto3);

        //then
        List<Lecture> lectures = lectureService.findByInstructorName(instructor.getMember().getName());
        List<Lecture> findLecture = lectureService.findByName("lecture1");
        assertThat(lectures.size()).isEqualTo(3);
        assertThat(findLecture).contains(savedLecture);
    }

    @Test
    void findAllStudents() {
    }
}