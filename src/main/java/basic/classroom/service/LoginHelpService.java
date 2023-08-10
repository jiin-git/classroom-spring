package basic.classroom.service;

import basic.classroom.dto.FindIdDto;
import basic.classroom.dto.FindPwDto;
import basic.classroom.domain.Instructor;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import basic.classroom.repository.jpa.InstructorRepository;
import basic.classroom.repository.jpa.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHelpService {

    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    private final InstructorJpaRepository instructorJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

//    @Transactional
//    public List<String> findLoginIds(FindIdDto findIdDto) {
//        String name = findIdDto.getName();
//        String email = findIdDto.getEmail();
//        MemberStatus memberStatus = findIdDto.getMemberStatus();
//
//        if (memberStatus == MemberStatus.INSTRUCTOR) {
//            return getInstructorIds(name, email);
//        }
//
//        return getStudentIds(name, email);
//    }
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

    private List<String> getStudentIds(String name, String email) {
        List<Student> students = studentRepository.findByName(name);
        List<String> findStudents = students.stream().filter(s -> s.getMember().getEmail().equals(email))
                .map(fs -> fs.getMember().getLoginId()).toList();

        return findStudents;
    }

    private List<String> getInstructorIds(String name, String email) {
        List<Instructor> instructors = instructorRepository.findByName(name);
        List<String> findInstructors = instructors.stream().filter(i -> i.getMember().getEmail().equals(email))
                .map(fi -> fi.getMember().getLoginId()).toList();

        return findInstructors;
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

//    private String getStudentPassword(String loginId, String email) {
//        Optional<Student> findStudent = studentRepository.findByLoginId(loginId);
//        if (findStudent.isEmpty()) {
//            return null;
//        }
//
//        String findEmail = findStudent.get().getMember().getEmail();
//        if (!findEmail.equals(email)) {
//            return null;
//        }
//
//        String randomPassword = RandomStringUtils.random(8, true, true);
//        Student student = findStudent.get();
//        student.getMember().setPassword(randomPassword);
//
//        return randomPassword;
//    }
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

//    private String getInstructorPassword(String loginId, String email) {
//        Optional<Instructor> findInstructor = instructorRepository.findByLoginId(loginId);
//        if (findInstructor.isEmpty()) {
//            return null;
//        }
//
//        String findEmail = findInstructor.get().getMember().getEmail();
//        if (!findEmail.equals(email)) {
//            return null;
//        }
//
//        String randomPassword = RandomStringUtils.random(8, true, true);
//        Instructor instructor = findInstructor.get();
//        instructor.getMember().setPassword(randomPassword);
//
//        return randomPassword;
//    }
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
