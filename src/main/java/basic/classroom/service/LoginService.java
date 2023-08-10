package basic.classroom.service;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import basic.classroom.dto.LoginDto;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import basic.classroom.repository.jpa.InstructorRepository;
import basic.classroom.repository.jpa.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    private final StudentJpaRepository studentJpaRepository;
    private final InstructorJpaRepository instructorJpaRepository;

//    @Transactional
//    public Student studentLogin(LoginDto loginDto) {
//        return studentRepository.findByLoginId(loginDto.getLoginId())
//                .filter(s -> s.getMember().getPassword().equals(loginDto.getLoginPw()))
//                .orElse(null);
//    }
    @Transactional
    public Student studentLogin(LoginDto loginDto) {
        return studentJpaRepository.findByMember_LoginIdAndMember_Password(loginDto.getLoginId(), loginDto.getLoginPw())
                .orElse(null);
    }

    @Transactional
    public Instructor instructorLogin(LoginDto loginDto) {
        return instructorJpaRepository.findByMember_LoginIdAndMember_Password(loginDto.getLoginId(), loginDto.getLoginPw())
                .orElse(null);
    }
}
