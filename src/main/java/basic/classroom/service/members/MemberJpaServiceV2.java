package basic.classroom.service.members;

import basic.classroom.domain.*;
import basic.classroom.dto.CreateMember.CreateMemberRequest;
import basic.classroom.dto.UpdateMyPageRequest;
import basic.classroom.dto.UpdatePasswordRequest;
import basic.classroom.exception.*;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import basic.classroom.service.ValidImageType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberJpaServiceV2 {
    private final InstructorJpaRepository instructorRepository;
    private final StudentJpaRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Transactional
    public Long create(CreateMemberRequest createMemberRequest) {
        validateCreateMemberRequest(createMemberRequest);
        Member member = Member.builder()
                .loginId(createMemberRequest.getLoginId())
                .password(passwordEncoder.encode(createMemberRequest.getPassword()))
                .name(createMemberRequest.getName())
                .email(createMemberRequest.getEmail()).build();
        MemberStatus memberStatus = createMemberRequest.getMemberStatus();
        validateDuplicatedMember(member.getLoginId());

        if (memberStatus == MemberStatus.INSTRUCTOR) {
            Instructor instructor = Instructor.createInstructor(member);
            instructorRepository.save(instructor);
            return instructor.getId();
        } else if (memberStatus == MemberStatus.STUDENT) {
            Student student = Student.createStudent(member);
            studentRepository.save(student);
            return student.getId();
        } else {
            throw new InvalidMemberStatusException(ErrorCode.INVALID_MEMBER_STATUS);
        }
    }
    private void validateCreateMemberRequest(CreateMemberRequest createMemberRequest) {
        Set<ConstraintViolation<CreateMemberRequest>> violations = validator.validate(createMemberRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    private void validateDuplicatedMember(String loginId) {
        Optional<Student> duplicatedStudent = studentRepository.findByMember_LoginId(loginId);
        Optional<Instructor> duplicatedInstructor = instructorRepository.findByMember_LoginId(loginId);

        if (duplicatedStudent.isPresent() || duplicatedInstructor.isPresent()) {
            throw new CreateDuplicatedMemberException(ErrorCode.DUPLICATED_LOGIN_ID);
        }
    }

    @Transactional
    public void update(Instructor instructor, UpdateMyPageRequest updateMyPageRequest) {
        validateUpdateMyPageRequest(updateMyPageRequest);
        MultipartFile imageFile = updateMyPageRequest.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            validateImageDataType(imageFile);
        }

        ProfileImage profileImage = getProfileImage(imageFile);
        String email = updateMyPageRequest.getEmail();
        instructor.updateInstructor(email, profileImage);
    }
    @Transactional
    public void update(Student student, UpdateMyPageRequest updateMyPageRequest) {
        validateUpdateMyPageRequest(updateMyPageRequest);
        MultipartFile imageFile = updateMyPageRequest.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            validateImageDataType(imageFile);
        }

        ProfileImage profileImage = getProfileImage(imageFile);
        String email = updateMyPageRequest.getEmail();
        student.updateStudent(email, profileImage);
    }
    private void validateUpdateMyPageRequest(UpdateMyPageRequest updateMyPageRequest) {
        Set<ConstraintViolation<UpdateMyPageRequest>> violations = validator.validate(updateMyPageRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
    private ProfileImage getProfileImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
            String contentType = imageFile.getContentType();
            byte[] imageBytes = imageFile.getBytes();

            return ProfileImage.builder()
                    .imageName(imageName)
                    .dataType(contentType)
                    .imageData(imageBytes)
                    .build();
        } catch (IOException e) {
            throw new ConvertImageException(ErrorCode.FAILED_CONVERT_PROFILE_IMAGE);
        }
    }
    private void validateImageDataType(MultipartFile imageFile) {
        try {
            String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename()).toUpperCase();
            ValidImageType validImageType = ValidImageType.valueOf(extension);
        } catch (IllegalArgumentException e) {
            throw new StoreImageException(ErrorCode.FAILED_STORE_IMAGE);
        }
    }

    @Transactional
    public void updatePassword(Instructor instructor, String password) {
        instructor.updatePassword(passwordEncoder.encode(password));
    }
    @Transactional
    public void updatePassword(Student student, String password) {
        student.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void updatePassword(Instructor instructor, UpdatePasswordRequest updatePasswordRequest) {
        validateUpdatePasswordRequest(updatePasswordRequest);
        instructor.updatePassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
    }
    @Transactional
    public void updatePassword(Student student, UpdatePasswordRequest updatePasswordRequest) {
        validateUpdatePasswordRequest(updatePasswordRequest);
        student.updatePassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
    }
    private void validateUpdatePasswordRequest(UpdatePasswordRequest updatePasswordRequest) {
        Set<ConstraintViolation<UpdatePasswordRequest>> violations = validator.validate(updatePasswordRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        String password = updatePasswordRequest.getPassword();
        String checkPassword = updatePasswordRequest.getCheckPassword();
        if (!password.equals(checkPassword)) {
            throw new UpdateMemberException(ErrorCode.FAILED_CHANGE_PASSWORD);
        }
    }

    @Transactional
    public void initializeProfile(Instructor instructor) {
        if (instructor.getProfileImage() != null) {
            instructor.clearProfileImage();
        }
    }
    @Transactional
    public void initializeProfile(Student student) {
        if (student.getProfileImage() != null) {
            student.clearProfileImage();
        }
    }

    @Transactional(readOnly = true)
    public Instructor findInstructor(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
    @Transactional(readOnly = true)
    public Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Instructor findInstructorByLoginId(String loginId) {
        return instructorRepository.findByMember_LoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
    @Transactional(readOnly = true)
    public Student findStudentByLoginId(String loginId) {
        return studentRepository.findByMember_LoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
