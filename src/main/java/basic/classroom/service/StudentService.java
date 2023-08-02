package basic.classroom.service;

import basic.classroom.domain.*;
import basic.classroom.dto.UpdateMemberDto;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import basic.classroom.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final LectureStudentMapperRepository mapperRepository;

    @Transactional
    public Long join(Student student) {
        String studentLoginId = student.getMember().getLoginId();
        Optional<Student> duplicateStudent = studentRepository.findByLoginId(studentLoginId);

        // 중복 회원 존재시 에러
        if (duplicateStudent.isPresent()) {
            log.info("duplicated!");
            throw new IllegalStateException();
        }

        // 성공 로직
        studentRepository.save(student);
        return student.getId();
    }

    public Student findOne(Long id) {
        return studentRepository.findOne(id);
    }

    @Transactional
    public void update(Long id, UpdateMemberDto updateMemberDto) {
        Student student = studentRepository.findOne(id);
        student.getMember().setEmail(updateMemberDto.getEmail());

        if (updateMemberDto.getImageFile() != null && !updateMemberDto.getImageFile().isEmpty()) {
            try {
                saveStudentImageFile(updateMemberDto.getImageFile(), student);
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

    private static void saveStudentImageFile(MultipartFile imageFile, Student student) throws IOException {
        ProfileImage profileImage = validProfileImage(imageFile);
        student.setProfileImage(profileImage);
    }

    private static ProfileImage validProfileImage(MultipartFile imageFile) throws IOException {
        ProfileImage profileImage = getProfileImage(imageFile);
        List<String> validImageTypes = getValidImageTypes();
        if (!validImageTypes.contains(imageFile.getContentType())) {
            throw new StoreImageException();
        }

        return profileImage;
    }

    private static ProfileImage getProfileImage(MultipartFile imageFile) throws IOException {
        String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
        String contentType = imageFile.getContentType();
        byte[] imageBytes = imageFile.getBytes();

        return new ProfileImage(imageName, contentType, imageBytes);
    }

    private static List<String> getValidImageTypes() {
        List<String> validImageTypes = new ArrayList<>();
        ValidImageType[] values = ValidImageType.values();
        for (ValidImageType value : values) {
            String dataType = "image/" + value.toString().toLowerCase();
            validImageTypes.add(dataType);
        }
        return validImageTypes;
    }

    @Transactional
    public void initializeProfile(Long id) {
        Student student = studentRepository.findOne(id);
        if (student.getProfileImage() != null) {
            student.getProfileImage().setImageName(null);
            student.getProfileImage().setDataType(null);
            student.getProfileImage().setImageData(null);
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        Student student = studentRepository.findOne(id);
        student.getMember().setPassword(password);
    }

    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    @Transactional
    public Long addLecture(Long studentId, Long lectureId) {
        // 엔티티 조회
        Student student = studentRepository.findOne(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        // mapper 생성
        LectureStudentMapper mapper = new LectureStudentMapper();
        mapper.setStudent(student);
        mapper.setLecture(lecture);

        // mapper 저장 및 설정
        mapperRepository.save(mapper);

        student.applyLecture(mapper);
        lecture.addStudent(mapper);

        return lectureId;
    }

    @Transactional
    public void cancelLecture(Long studentId, Long lectureId) {
        // 엔티티 조회
        Student student = studentRepository.findOne(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        // mapper 제거
        LectureStudentMapper mapper = student.getApplyingLectures().get(lectureId);
        mapperRepository.cancel(mapper);

        student.cancelLecture(lectureId);
        lecture.removeStudent(studentId);
    }

    public List<Lecture> findAllLectures(Long id) {
        Student student = studentRepository.findOne(id);
        List<Lecture> lectures = student.findAllLectures();

        return lectures;
    }
}
