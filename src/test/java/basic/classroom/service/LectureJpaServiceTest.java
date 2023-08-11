package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.LectureStudentMapperJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import basic.classroom.service.datajpa.LectureJpaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LectureJpaServiceTest {
    @Autowired LectureJpaService lectureJpaService;
    @Autowired LectureStudentMapperJpaRepository mapperJpaRepository;
    @Autowired InstructorJpaRepository instructorJpaRepository;
    @Autowired StudentJpaRepository studentJpaRepository;

    @Test
    void BasicCRUDTest() {
        Instructor instructor = Instructor.createInstructor(new Member("instructor", "loginId", "password", "email@email.com"));
        instructorJpaRepository.save(instructor);

        AddLectureDto lectureDto = new AddLectureDto("lecture", 10, LectureStatus.OPEN);
        Long lectureId = lectureJpaService.create(instructor, lectureDto);
        Lecture findLecture = lectureJpaService.findOne(lectureId);

        assertThat(lectureId).isEqualTo(findLecture.getId());
        assertThat(findLecture.getName()).isEqualTo(lectureDto.getName());
        assertThat(findLecture.getInstructor()).isEqualTo(instructor);
    }

    @Test
    void StudentApplyAndCancelLectureTest() {
        Instructor instructor = Instructor.createInstructor(new Member("instructor", "loginId", "password", "email@email.com"));
        instructorJpaRepository.save(instructor);
        Student student = Student.createStudent(new Member("student", "studentId", "password", "student@email.com"));
        studentJpaRepository.save(student);

        AddLectureDto lectureDto = new AddLectureDto("lecture", 10, LectureStatus.OPEN);
        Long lectureId = lectureJpaService.create(instructor, lectureDto);
        Lecture findLecture = lectureJpaService.findOne(lectureId);

        lectureJpaService.applyLecture(student, lectureId);
        Page<Lecture> findApplyLectures = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(student.getId(), PageRequest.of(0, 1));
        assertThat(findApplyLectures.getContent()).contains(findLecture);

        lectureJpaService.cancelLecture(student, lectureId);
        Page<Lecture> findCancelLecture = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(student.getId(), PageRequest.of(0, 1));
        assertThat(findCancelLecture.getContent()).isEmpty();
    }

    @Test
    void findLecturesByConditionAndPagingTest() {
        Instructor instructorA = Instructor.createInstructor(new Member("instructorA", "loginId", "password", "email@email.com"));
        Instructor instructorB = Instructor.createInstructor(new Member("instructorB", "loginId", "password", "email@email.com"));
        instructorJpaRepository.save(instructorA);
        instructorJpaRepository.save(instructorB);

        for (int i = 1; i <= 6; i++) {
            String name = "lecture" + i;
            LectureStatus lectureStatus = LectureStatus.READY;
            if (i % 2 == 0) {
                lectureStatus = LectureStatus.OPEN;
            } else if (i % 3 == 0) {
                lectureStatus = LectureStatus.FULL;
            }
            AddLectureDto lectureDto = new AddLectureDto(name, 10, lectureStatus);

            if (i >= 5) {
                lectureJpaService.create(instructorB, lectureDto);
            } else {
                lectureJpaService.create(instructorA, lectureDto);
            }
        }

        int pageSize = 2;
        SearchConditionDto lectureStatusCondition = new SearchConditionDto(LectureStatus.OPEN.name(), null, null, 2);
        Page<Lecture> findByLectureStatus = lectureJpaService.findPersonalizedLectures(lectureStatusCondition, pageSize);
        assertThat(findByLectureStatus.getTotalPages()).isEqualTo(2);
        assertThat(findByLectureStatus.getTotalElements()).isEqualTo(3);
        assertThat(findByLectureStatus.getNumberOfElements()).isEqualTo(1);

        SearchConditionDto InstructorNameCondition = new SearchConditionDto(null, LectureSearchCondition.INSTRUCTOR.name(), instructorA.getMember().getName(), 2);
        Page<Lecture> findByInstructorNameCondition = lectureJpaService.findPersonalizedLectures(InstructorNameCondition, pageSize);
        assertThat(findByInstructorNameCondition.getTotalPages()).isEqualTo(2);
        assertThat(findByInstructorNameCondition.getTotalElements()).isEqualTo(4);
        assertThat(findByInstructorNameCondition.getNumberOfElements()).isEqualTo(2);

        SearchConditionDto LectureStatusAndInstructorNameCondition = new SearchConditionDto(LectureStatus.OPEN.name(), LectureSearchCondition.INSTRUCTOR.name(), instructorA.getMember().getName(), 1);
        Page<Lecture> findByLectureStatusAndInstructorNameCondition = lectureJpaService.findPersonalizedLectures(LectureStatusAndInstructorNameCondition, pageSize);
        assertThat(findByLectureStatusAndInstructorNameCondition.getTotalPages()).isEqualTo(1);
        assertThat(findByLectureStatusAndInstructorNameCondition.getTotalElements()).isEqualTo(2);
        assertThat(findByLectureStatusAndInstructorNameCondition.getNumberOfElements()).isEqualTo(2);

        SearchConditionDto LectureStatusAndLectureNameCondition = new SearchConditionDto(LectureStatus.READY.name(), LectureSearchCondition.LECTURE.name(), "lecture1", 1);
        Page<Lecture> findByLectureStatusAndLectureNameCondition = lectureJpaService.findPersonalizedLectures(LectureStatusAndLectureNameCondition, pageSize);
        assertThat(findByLectureStatusAndLectureNameCondition.getTotalPages()).isEqualTo(1);
        assertThat(findByLectureStatusAndLectureNameCondition.getTotalElements()).isEqualTo(1);
        assertThat(findByLectureStatusAndLectureNameCondition.getNumberOfElements()).isEqualTo(1);

    }
}