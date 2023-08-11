package basic.classroom.service.datajpa;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.FindIdDto;
import basic.classroom.dto.FindPwDto;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHelpJpaService {
    private final InstructorJpaRepository instructorJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    @Transactional
    public List<String> findLoginIds(FindIdDto findIdDto) {
        String name = findIdDto.getName();
        String email = findIdDto.getEmail();
        MemberStatus memberStatus = findIdDto.getMemberStatus();

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            return instructorJpaRepository.findLoginIdByNameAndEmail(name, email);
        }
        return studentJpaRepository.findLoginIdByNameAndEmail(name, email);
    }

    @Transactional
    public String findLoginPw(FindPwDto findPwDto) {
        String loginId = findPwDto.getLoginId();;
        String email = findPwDto.getEmail();
        MemberStatus memberStatus = findPwDto.getMemberStatus();

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            return getInstructorPassword(loginId, email);
        }
        return getStudentPassword(loginId, email);
    }

    private String getStudentPassword(String loginId, String email) {
        Optional<Student> findStudent = studentJpaRepository.findByMember_LoginIdAndMember_Email(loginId, email);
        if (findStudent.isEmpty()) {
            return null;
        }

        String randomPassword = RandomStringUtils.random(8, true, true);
        Student student = findStudent.get();
        student.getMember().setPassword(randomPassword);
        return randomPassword;
    }

    private String getInstructorPassword(String loginId, String email) {
        Optional<Instructor> findInstructor = instructorJpaRepository.findByMember_LoginIdAndMember_Email(loginId, email);
        if (findInstructor.isEmpty()) {
            return null;
        }

        String randomPassword = RandomStringUtils.random(8, true, true);
        Instructor instructor = findInstructor.get();
        instructor.getMember().setPassword(randomPassword);

        return randomPassword;
    }
}
