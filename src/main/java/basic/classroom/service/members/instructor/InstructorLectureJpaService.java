package basic.classroom.service.members.instructor;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureRequest;
import basic.classroom.dto.ApplicantsResponse;
import basic.classroom.dto.UpdateLecture.UpdateLectureDto;
import basic.classroom.dto.UpdateLecture.UpdateLectureRequest;
import basic.classroom.exception.ConvertImageException;
import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.StoreImageException;
import basic.classroom.exception.UpdateLectureException;
import basic.classroom.repository.dataJpa.LectureJpaRepository;
import basic.classroom.service.ValidImageType;
import basic.classroom.service.members.MemberJpaServiceV2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static basic.classroom.dto.LectureResponse.InstructorLectureBasicResponse;
import static basic.classroom.dto.LectureResponse.InstructorLectureDetailsResponse;

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
            throw new ConstraintViolationException(violations);
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

    @Transactional(readOnly = true)
    public Page<InstructorLectureBasicResponse> findMyLecturesByPage(String loginId, Long page) {
        Instructor instructor = findInstructor(loginId);
        List<Lecture> myLectures = instructor.getAllLectures();

        int currentPage = page == null ? 1 : page.intValue();
        int pageSize = 10;

        PagedListHolder pagedListHolder = new PagedListHolder(myLectures);
        pagedListHolder.setPage(currentPage - 1);
        pagedListHolder.setPageSize(pageSize);
        List<Lecture> pageList = pagedListHolder.getPageList();

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Lecture> lectures = new PageImpl<>(pageList, pageable, myLectures.size());
        Page<InstructorLectureBasicResponse> lectureBasicResponses = lectures.map(InstructorLectureBasicResponse::fromLecture);

        return lectureBasicResponses;
    }
    @Transactional(readOnly = true)
    public InstructorLectureDetailsResponse getLectureInfo(String loginId, Long lectureId) {
        Instructor instructor = findInstructor(loginId);
        Lecture lecture = instructor.getLecture(lectureId);
        InstructorLectureDetailsResponse lectureDetailsResponse = InstructorLectureDetailsResponse.fromLecture(lecture);
        return lectureDetailsResponse;
    }
    @Transactional(readOnly = true)
    public List<ApplicantsResponse> getApplicants(String loginId, Long lectureId) {
        Instructor instructor = findInstructor(loginId);
        Lecture lecture = instructor.getLecture(lectureId);

        List<ApplicantsResponse> applicants = new ArrayList<>();
        Map<Long, LectureStudentMapper> appliedStudents = lecture.getAppliedStudents();
        appliedStudents.forEach((key, mapper) ->
                applicants.add(ApplicantsResponse.fromStudent(mapper.getStudent())));

        return applicants;
    }

    @Transactional
    public void updateLecture(String loginId, Long lectureId, UpdateLectureRequest updateLectureRequest) {
        Instructor instructor = findInstructor(loginId);
        Lecture lecture = instructor.getLecture(lectureId);
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
            throw new ConstraintViolationException(violations);
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
        int updatePersonnel = updateLectureRequest.getPersonnel().intValue();
        int remainingPersonnel = lecture.getRemainingPersonnel();
        int personnel = lecture.getPersonnel();
        int appliedPersonnel = personnel - remainingPersonnel;
        int updateRemainingPersonnel = updatePersonnel - appliedPersonnel;

        if (updateRemainingPersonnel < 0) {
            throw new UpdateLectureException(ErrorCode.FAILED_CHANGE_LECTURE_PERSONNEL);
        }

        return updateRemainingPersonnel;
    }

    private Instructor findInstructor(String loginId) {
        return memberService.findInstructorByLoginId(loginId);
    }
}
