package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InstructorServiceTest {

    @Autowired InstructorService instructorService;
    @Autowired InstructorRepository instructorRepository;

    @Test
    void 회원가입() throws Exception {
        //given
        Instructor instructor = new Instructor(new Member("name", "id", "pw", "email"));

        //when
        instructorService.join(instructor);

        //then
        Instructor findInstructor = instructorRepository.findOne(instructor.getId());
        assertThat(findInstructor).isEqualTo(instructor);
    }

    @Test
    void 회원수정() {
        //given
        Instructor instructor = new Instructor(new Member("name", "id", "pw", "email"));
        instructorService.join(instructor);

        //when
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(instructor);
        instructorService.update(instructor.getId(), updateMemberDto);

        //then
        Instructor findInstructor = instructorRepository.findOne(instructor.getId());
        assertThat(findInstructor.getMember().getName()).isEqualTo(updateMemberDto.getName());
        assertThat(findInstructor.getMember().getPassword()).isEqualTo(updateMemberDto.getPassword());
    }

    @Test
    void 강의추가() {
        //given
        Instructor instructor = new Instructor(new Member("name", "id", "pw", "email"));
        instructorService.join(instructor);


        //when
        AddLectureDto lectureDto = new AddLectureDto("lecture", 10, LectureStatus.READY);
        instructorService.addLecture(instructor.getId(), lectureDto);

        //then
        List<Lecture> lectures = instructorService.findLectures(instructor.getId());
//        assertThat(lectures).contains(lecture);
        assertThat(lectures.size()).isEqualTo(1);
    }
}