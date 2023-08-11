package basic.classroom.service;

import basic.classroom.domain.Member;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.FindIdDto;
import basic.classroom.dto.FindPwDto;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import basic.classroom.service.datajpa.LoginHelpJpaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class LoginHelpJpaServiceTest {

    @Autowired LoginHelpJpaService loginHelpService;
    @Autowired StudentJpaRepository studentJpaRepository;

    @Test
    void findLoginIds() {
        Student student = Student.createStudent(new Member("student", "studentId", "password", "email@email.com"));
        Student savedStudent = studentJpaRepository.save(student);

        String name = savedStudent.getMember().getName();
        String email = savedStudent.getMember().getEmail();

        FindIdDto findIdDto = new FindIdDto(name, email, MemberStatus.STUDENT);
        List<String> findLoginIds = loginHelpService.findLoginIds(findIdDto);

        Assertions.assertThat(findLoginIds).contains(student.getMember().getLoginId());
    }

    @Test
    void findLoginPw() {
        Student student = Student.createStudent(new Member("student", "studentId2", "password", "email@email.com"));
        String originalPassword = student.getMember().getPassword();
        Student savedStudent = studentJpaRepository.save(student);

        String loginId = savedStudent.getMember().getLoginId();
        String email = savedStudent.getMember().getEmail();

        FindPwDto findPwDto = new FindPwDto(loginId, email, MemberStatus.STUDENT);
        String newPassword = loginHelpService.findLoginPw(findPwDto);

        Assertions.assertThat(newPassword).isEqualTo(student.getMember().getPassword());
        Assertions.assertThat(originalPassword).isNotEqualTo(student.getMember().getPassword());
    }
}