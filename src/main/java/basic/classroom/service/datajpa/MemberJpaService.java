package basic.classroom.service.datajpa;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.exception.CreateDuplicatedMemberException;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static basic.classroom.service.ProfileImageService.initializeMemberProfile;
import static basic.classroom.service.ProfileImageService.saveImageFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberJpaService {
    private final StudentJpaRepository studentJpaRepository;
    private final InstructorJpaRepository instructorJpaRepository;

    @Transactional
    public Long join(Student student) {
        String loginId = student.getMember().getLoginId();
        Optional<Student> duplicateStudent = studentJpaRepository.findByMember_LoginId(loginId);

        // 중복 회원 존재시 에러
        if (duplicateStudent.isPresent()) {
            throw new CreateDuplicatedMemberException("중복 가입 ID 입니다. 다시 가입해주세요.");
        }

        // 성공 로직
        studentJpaRepository.save(student);
        return student.getId();
    }

    @Transactional
    public Long join(Instructor instructor) {
        String loginId = instructor.getMember().getLoginId();
        Optional<Instructor> duplicateInstructor = instructorJpaRepository.findByMember_LoginId(loginId);

        // 중복 회원 존재시 에러
        if (duplicateInstructor.isPresent()) {
            throw new CreateDuplicatedMemberException("중복 가입 ID 입니다. 다시 가입해주세요.");
        }

        // 성공 로직
        instructorJpaRepository.save(instructor);
        return instructor.getId();
    }

    @Transactional
    public void update(Instructor instructor, UpdateMemberDto updateMemberDto) {
        instructor.getMember().setEmail(updateMemberDto.getEmail());

        if (updateMemberDto.getImageFile() != null && !updateMemberDto.getImageFile().isEmpty()) {
            try {
                saveImageFile(updateMemberDto.getImageFile(), instructor);
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

    @Transactional
    public void update(Student student, UpdateMemberDto updateMemberDto) {
        student.getMember().setEmail(updateMemberDto.getEmail());

        if (updateMemberDto.getImageFile() != null && !updateMemberDto.getImageFile().isEmpty()) {
            try {
                saveImageFile(updateMemberDto.getImageFile(), student);
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

    @Transactional
    public void updatePassword(Instructor instructor, String password) {
        instructor.getMember().setPassword(password);
    }

    @Transactional
    public void updatePassword(Student student, String password) {
        student.getMember().setPassword(password);
    }

    @Transactional
    public void initializeProfile(Instructor instructor) {
        if (instructor.getProfileImage() != null) {
            initializeMemberProfile(instructor);
        }
    }

    @Transactional
    public void initializeProfile(Student student) {
        if (student.getProfileImage() != null) {
            initializeMemberProfile(student);
        }
    }

    public Instructor findInstructor(Long id) {
        return instructorJpaRepository.findById(id).get();
    }
    public Student findStudent(Long id) {
        return studentJpaRepository.findById(id).get();
    }
}
