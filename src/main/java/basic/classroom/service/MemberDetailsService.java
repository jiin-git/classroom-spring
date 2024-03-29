package basic.classroom.service;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.MemberStatus;
import basic.classroom.domain.Student;
import basic.classroom.dto.MemberDetails;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final InstructorJpaRepository instructorRepository;
    private final StudentJpaRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("username = {}", username);
        Optional<Instructor> findInstructor = instructorRepository.findByMember_LoginId(username);
        Optional<Student> findStudent = studentRepository.findByMember_LoginId(username);

        if (findInstructor.isPresent()) {
            Instructor instructor = findInstructor.get();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(MemberStatus.INSTRUCTOR.name()));

            return MemberDetails.builder()
                    .username(instructor.getMember().getLoginId())
                    .password(instructor.getMember().getPassword())
                    .authorities(roles)
                    .build();

        } else if (findStudent.isPresent()) {
            Student student = findStudent.get();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(MemberStatus.STUDENT.name()));

            return MemberDetails.builder()
                    .username(student.getMember().getLoginId())
                    .password(student.getMember().getPassword())
                    .authorities(roles)
                    .build();

        } else {
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다.");
        }
    }
}
