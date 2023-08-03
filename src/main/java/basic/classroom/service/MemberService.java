package basic.classroom.service;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.exception.CreateDuplicatedMemberException;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.InstructorRepository;
import basic.classroom.repository.StudentRepository;
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
public class MemberService {
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    @Transactional
    public Long join(Student student) {
        String loginId = student.getMember().getLoginId();
        Optional<Student> duplicateStudent = studentRepository.findByLoginId(loginId);

        // 중복 회원 존재시 에러
        if (duplicateStudent.isPresent()) {
            log.info("duplicated!");
            throw new IllegalStateException();
        }

        // 성공 로직
        studentRepository.save(student);
        return student.getId();
    }

    @Transactional
    public Long join(Instructor instructor) {
        String loginId = instructor.getMember().getLoginId();
        Optional<Instructor> duplicateInstructor = instructorRepository.findByLoginId(loginId);

        // 중복 회원 존재시 에러
        if (duplicateInstructor.isPresent()) {
            throw new CreateDuplicatedMemberException("중복 가입 ID 입니다. 다시 가입해주세요.");
        }

        // 성공 로직
        instructorRepository.save(instructor);
        return instructor.getId();
    }

    public Instructor findInstructor(Long id) {
        return instructorRepository.findOne(id);
    }
    public Student findStudent(Long id) {
        return studentRepository.findOne(id);
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
}
