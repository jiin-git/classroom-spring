package basic.classroom.service;

import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.Member;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.LoginDto;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired LoginService loginService;
    @Autowired StudentJpaRepository studentJpaRepository;

    @Test
    void studentLogin() {
        Student student = Student.createStudent(new Member("student", "stuID", "pw", "email"));
        studentJpaRepository.save(student);

        Student savedStudent = studentJpaRepository.findById(student.getId()).get();
        String loginId = savedStudent.getMember().getLoginId();
        String password = savedStudent.getMember().getPassword();
        LoginDto loginDto = new LoginDto(loginId, password, MemberStatus.STUDENT);

        Student loginStudent = loginService.studentLogin(loginDto);
        Assertions.assertThat(loginStudent).isEqualTo(student);
    }
}