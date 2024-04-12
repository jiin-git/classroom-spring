package basic.classroom.service.members.instructor;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureRequest;
import basic.classroom.dto.UpdateLecture.*;
import basic.classroom.exception.*;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.LectureJpaRepository;
import basic.classroom.service.ValidImageType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstructorLectureJpaService {
    private final MemberJpaServiceV2 memberService;
    private final LectureJpaRepository lectureRepository;
    private final Validator validator;

    @Transactional
    public Long createLecture(String loginId, AddLectureRequest addLectureRequest) {
        Instructor instructor = findInstructor(loginId);

        validateAddLectureRequest(addLectureRequest);
        MultipartFile imageFile = addLectureRequest.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            validateImageDataType(imageFile);
        }

        ProfileImage profileImage = getProfileImage(imageFile);
        Lecture lecture = Lecture.createLecture(instructor, addLectureRequest, profileImage);
        lectureRepository.save(lecture);
        instructor.addLectures(lecture);
        return lecture.getId();
    }
    private void validateAddLectureRequest(AddLectureRequest addLectureRequest) {
        Set<ConstraintViolation<AddLectureRequest>> violations = validator.validate(addLectureRequest);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<AddLectureRequest> violation : violations) {
                stringBuilder.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Error : " + stringBuilder.toString(), violations);
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
    private ProfileImage getProfileImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
            String contentType = imageFile.getContentType();
            byte[] imageBytes = imageFile.getBytes();

            return ProfileImage.builder()
                    .imageName(imageName).dataType(contentType).imageData(imageBytes).build();
        } catch (IOException e) {
            throw new ConvertImageException(ErrorCode.FAILED_CONVERT_PROFILE_IMAGE);
        }
    }

    // entity method에 포함되어 있어 굳이 필요 없음
    public Lecture findMyLecture(Instructor instructor, Long lectureId) {
        return instructor.getLecture(lectureId);
    }
    public List<Lecture> findMyAllLectures(Instructor instructor) {
        return instructor.getAllLectures();
    }

    public Page<Lecture> findMyLecturesByPage(Long instructorId, Long page) {
        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = instructorJpaRepository.findLecturesById(instructorId, pageable);
        return lectures;
    }
    public List<Student> findApplicantsByLectureId(Long lectureId) {
        List<Student> applicants = lectureJpaRepository.findStudentsById(lectureId);
        return applicants;
    }

    @Transactional
    public void updateLecture(Long lectureId, Long instructorId, UpdateLectureRequest updateLectureRequest) {
        Lecture lecture = lectureJpaRepository.findById(lectureId).orElse(null);
        if (!lecture.getInstructorId().equals(instructorId)) {
            throw new UnauthorizedLectureAccessException(ErrorCode.LECTURE_ACCESS_DENIED);
        }

        validateUpdateLectureRequest(updateLectureRequest);

        // memberStatus and personnel validation
        int updateRemainingPersonnel = getUpdateRemainingPersonnel(lecture, updateLectureRequest);
        LectureStatus updateLectureStatus = updateLectureRequest.getLectureStatus();
        validateLectureStatus(lecture, updateLectureStatus, updateRemainingPersonnel);

        // Image Validate
        MultipartFile imageFile = updateLectureRequest.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            validateImageDataType(imageFile);
        }

        ProfileImage profileImage = getProfileImage(imageFile);
        UpdateLectureDto updateLectureDto = UpdateLectureDto.fromRequest(updateLectureRequest, updateRemainingPersonnel, profileImage);
        lecture.updateLecture(updateLectureDto);
    }
    private void validateUpdateLectureRequest(UpdateLectureRequest updateLectureRequest) {
        Set<ConstraintViolation<UpdateLectureRequest>> violations = validator.validate(updateLectureRequest);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<UpdateLectureRequest> violation : violations) {
                stringBuilder.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Error : " + stringBuilder.toString(), violations);
        }
    }
    private void validateLectureStatus(Lecture lecture, LectureStatus updateLectureStatus, int updateRemainingPersonnel) {
        int applicantsCount = lecture.getAppliedStudents().size();
        if (updateLectureStatus.equals(LectureStatus.READY) && applicantsCount > 0) {
            throw new UpdateLectureException(ErrorCode.FAILED_CHANGE_LECTURE_STATUS_READY);
        }
        else if (updateLectureStatus.equals(LectureStatus.OPEN) && updateRemainingPersonnel == 0) {
            throw new UpdateLectureException(ErrorCode.FAILED_CHANGE_LECTURE_STATUS_OPEN);
        }
        else if (updateLectureStatus.equals(LectureStatus.FULL) && updateRemainingPersonnel != 0) {
            throw new UpdateLectureException(ErrorCode.FAILED_CHANGE_LECTURE_STATUS_FULL);
        }
    }
    private int getUpdateRemainingPersonnel(Lecture lecture, UpdateLectureRequest updateLectureRequest) {
        int updatePersonnel = updateLectureRequest.getPersonnel();
        int remainingPersonnel = lecture.getRemainingPersonnel();
        int personnel = lecture.getPersonnel();
        int appliedPersonnel = personnel - remainingPersonnel;
        int updateRemainingPersonnel = updatePersonnel - appliedPersonnel;

        if (updateRemainingPersonnel < 0) {
            throw new UpdateLectureException(ErrorCode.FAILED_CHANGE_LECTURE_PERSONNEL);
        }

        return updateRemainingPersonnel;
    }
}
