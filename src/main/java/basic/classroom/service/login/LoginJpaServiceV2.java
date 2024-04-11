package basic.classroom.service.login;

import basic.classroom.config.JwtTokenProvider;
import basic.classroom.domain.Instructor;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.LoginRequest;
import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.InvalidMemberStatusException;
import basic.classroom.exception.MemberNotFoundException;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static basic.classroom.dto.FindIds.FindIdsRequest;
import static basic.classroom.dto.FindPassword.FindPasswordRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginJpaServiceV2 {
    private final InstructorJpaRepository instructorJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Transactional
    public ResponseToken login(LoginRequest loginRequest) {
        validateLoginRequest(loginRequest);

        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();
        MemberStatus memberStatus = loginRequest.getMemberStatus();

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            Instructor findInstructor = instructorJpaRepository.findByMember_LoginId(loginId).orElse(null);
            return getMemberResponseToken(loginId, password, findInstructor);
        } else if (memberStatus == MemberStatus.STUDENT) {
            Student findStudent = studentJpaRepository.findByMember_LoginId(loginId).orElse(null);
            return getMemberResponseToken(loginId, password, findStudent);
        } else {
            throw new InvalidMemberStatusException(ErrorCode.INVALID_MEMBER_STATUS);
        }
    }
    private void validateLoginRequest(LoginRequest loginRequest) {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    private ResponseToken getMemberResponseToken(String loginId, String password, Instructor findInstructor) {
        if (findInstructor == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            String findInstructorPassword = findInstructor.getMember().getPassword();
            return getResponseToken(loginId, password, findInstructorPassword);
        }
    }
    private ResponseToken getMemberResponseToken(String loginId, String password, Student findStudent) {
        if (findStudent == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            String findStudentPassword = findStudent.getMember().getPassword();
            return getResponseToken(loginId, password, findStudentPassword);
        }
    }
    private ResponseToken getResponseToken(String loginId, String password, String findMemberPassword) {
        if (passwordEncoder.matches(password, findMemberPassword)) {
            return new ResponseToken(jwtTokenProvider.createToken(loginId));
        } else {
            throw new MemberNotFoundException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public List<String> findLoginIds(FindIdsRequest findIdsRequest) {
        validateFindIdsRequest(findIdsRequest);

        String name = findIdsRequest.getName();
        String email = findIdsRequest.getEmail();
        MemberStatus memberStatus = findIdsRequest.getMemberStatus();
        List<String> findLoginIds = new ArrayList<>();

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            findLoginIds.addAll(instructorJpaRepository.findLoginIdsByMember_NameAndMember_Email(name, email));
        } else if (memberStatus == MemberStatus.STUDENT) {
            findLoginIds.addAll(studentJpaRepository.findLoginIdsByMember_NameAndMember_Email(name, email));
        } else {
            throw new InvalidMemberStatusException(ErrorCode.INVALID_MEMBER_STATUS);
        }

        if (findLoginIds.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return findLoginIds;
    }
    private void validateFindIdsRequest(FindIdsRequest findIdsRequest) {
        Set<ConstraintViolation<FindIdsRequest>> violations = validator.validate(findIdsRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Transactional
    public String findLoginPassword(FindPasswordRequest findPasswordRequest) {
        validateLoginRequest(findPasswordRequest);

        String loginId = findPasswordRequest.getLoginId();
        String email = findPasswordRequest.getEmail();
        MemberStatus memberStatus = findPasswordRequest.getMemberStatus();

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            return getInstructorPassword(loginId, email);
        } else if (memberStatus == MemberStatus.STUDENT) {
            return getStudentPassword(loginId, email);
        } else {
            throw new InvalidMemberStatusException(ErrorCode.INVALID_MEMBER_STATUS);
        }
    }
    private void validateLoginRequest(FindPasswordRequest findPasswordRequest) {
        Set<ConstraintViolation<FindPasswordRequest>> violations = validator.validate(findPasswordRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    private String getStudentPassword(String loginId, String email) {
        Optional<Student> findStudent = studentJpaRepository.findByMember_LoginIdAndMember_Email(loginId, email);
        if (findStudent.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String randomPassword = RandomStringUtils.random(8, true, true);
        Student student = findStudent.get();
        student.updatePassword(passwordEncoder.encode(randomPassword));
        return randomPassword;
    }
    private String getInstructorPassword(String loginId, String email) {
        Optional<Instructor> findInstructor = instructorJpaRepository.findByMember_LoginIdAndMember_Email(loginId, email);
        if (findInstructor.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String randomPassword = RandomStringUtils.random(8, true, true);
        Instructor instructor = findInstructor.get();
        instructor.updatePassword(passwordEncoder.encode(randomPassword));
        return randomPassword;
    }
}
