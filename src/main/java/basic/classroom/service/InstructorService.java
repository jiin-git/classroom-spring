package basic.classroom.service;

import basic.classroom.dto.*;
import basic.classroom.domain.*;
import basic.classroom.exception.CreateDuplicatedMemberException;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.InstructorRepository;
import basic.classroom.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public Long join(Instructor instructor) {
        Optional<Instructor> duplicateInstructor = instructorRepository.findByLoginId(instructor.getMember().getLoginId());

        // 중복 회원 존재시 에러
        if (duplicateInstructor.isPresent()) {
//            Long error = -1L;
//            return error;
            throw new CreateDuplicatedMemberException("중복 가입 ID 입니다. 다시 가입해주세요.");
        }

        // 성공 로직
        instructorRepository.save(instructor);

        return instructor.getId();
    }

    public Instructor findOne(Long id) {
        return instructorRepository.findOne(id);
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Instructor instructor = instructorRepository.findOne(id);
        instructor.getMember().setEmail(updateMemberDto.getEmail());

        if (updateMemberDto.getImageFile() != null && !updateMemberDto.getImageFile().isEmpty()) {
            try {
                MultipartFile imageFile = updateMemberDto.getImageFile();
                String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
                String contentType = imageFile.getContentType();
                byte[] imageBytes = imageFile.getBytes();

                instructor.setProfileImage(new ProfileImage(imageName, contentType, imageBytes));
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

    @Transactional
    public void initializeProfile(Long id) {
        Instructor instructor = instructorRepository.findOne(id);
        if (instructor.getProfileImage() != null) {
            instructor.getProfileImage().setImageName(null);
            instructor.getProfileImage().setDataType(null);
            instructor.getProfileImage().setImageData(null);
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        Instructor instructor = instructorRepository.findOne(id);
        instructor.getMember().setPassword(password);
    }

    @Transactional
    public Long addLecture(Long instructorId, AddLectureDto addLectureDto) {
        Instructor instructor = instructorRepository.findOne(instructorId);
        Lecture lecture = Lecture.createLecture(addLectureDto.getName(), instructor, addLectureDto.getPersonnel(), addLectureDto.getLectureStatus());

        lectureRepository.save(lecture);
        instructor.addLectures(lecture);

        return lecture.getId();
    }

    @Transactional
    public void updateLecture(Long instructorId, UpdateLectureDto updateLectureDto, int updateRemainingPersonnel) {
        Instructor instructor = instructorRepository.findOne(instructorId);
        Lecture lecture = instructor.getLectures().get(updateLectureDto.getLectureId());

        lecture.setPersonnel(updateLectureDto.getPersonnel());
        lecture.setRemainingPersonnel(updateRemainingPersonnel);
        lecture.setLectureStatus(updateLectureDto.getLectureStatus());

        if (!updateLectureDto.getImageFile().isEmpty()) {
            try {
                MultipartFile imageFile = updateLectureDto.getImageFile();
                String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
                String contentType = imageFile.getContentType();
                byte[] imageBytes = imageFile.getBytes();

                lecture.setProfileImage(new ProfileImage(imageName, contentType, imageBytes));
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

    public List<Lecture> findLectures(Long id) {
        List<Lecture> lectures = instructorRepository.findOne(id).getLectures().values().stream().toList();
        return lectures;
    }
}
