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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Instructor> findLoginId(String name, String email) {
        List<Instructor> instructors = instructorRepository.findByName(name);
        List<Instructor> findInstructors = instructors.stream().filter(s -> s.getMember().getEmail() == email).collect(Collectors.toList());

        // 추가 검증 필요
        // 회원이 존재하지 않을 경우
        if (findInstructors.isEmpty()) {
            log.info("아이디를 찾을 수 없습니다.");
            throw new NoSuchElementException();
        }

        return findInstructors;
    }

    @Transactional
    public String findLoginPw(String loginId, String email) {
        Optional<Instructor> instructor = instructorRepository.findByLoginId(loginId);
        if (instructor.isEmpty()) {
            log.info("유효한 아이디가 없습니다.");
            throw new NoSuchElementException();
        }

        // 검증 코드 필요 + 랜덤 pw 생성 하여 리턴하게끔 추후 변경
        String findEmail = instructor.get().getMember().getEmail();
        if (!findEmail.equals(email)) {
            log.info("이메일이 틀렸습니다.");
            throw new IllegalStateException();
        }

        return instructor.get().getMember().getPassword();
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Instructor instructor = instructorRepository.findOne(id);
        instructor.getMember().setEmail(updateMemberDto.getEmail());

//        log.info("getImageFile = {}", updateMemberDto.getImageFile());
//        log.info("getImageFile is null? = {}", updateMemberDto.getImageFile() == null);

        if (updateMemberDto.getImageFile() != null && !updateMemberDto.getImageFile().isEmpty()) {
            try {
                MultipartFile imageFile = updateMemberDto.getImageFile();
                String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
                String contentType = imageFile.getContentType();
                byte[] imageBytes = imageFile.getBytes();

//                log.info("imageFile Name = {}", imageName);
//                log.info("contentType = {}", contentType);
//                log.info("imageBytes = {}", imageBytes);
                instructor.setProfileImage(new ProfileImage(imageName, contentType, imageBytes));
//                log.info("instructor getProfileImage = {}", instructor.getProfileImage());
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

//                log.info("imageFile Name = {}", imageName);
//                log.info("contentType = {}", contentType);
//                log.info("imageBytes = {}", imageBytes);
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
