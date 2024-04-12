//package basic.classroom.service.jpa;
//
//import basic.classroom.domain.*;
//import basic.classroom.dto.AddLectureDto;
//import basic.classroom.dto.SearchConditionDto;
//import basic.classroom.dto.UpdateLectureDto;
//import basic.classroom.exception.StoreImageException;
//import basic.classroom.repository.jpa.InstructorRepository;
//import basic.classroom.repository.jpa.LectureRepository;
//import basic.classroom.repository.jpa.LectureStudentMapperRepository;
//import basic.classroom.repository.jpa.StudentRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.util.List;
//
//import static basic.classroom.service.ProfileImageService.*;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class LectureService {
//    private final LectureRepository lectureRepository;
//    private final LectureStudentMapperRepository mapperRepository;
//    private final InstructorRepository instructorRepository;
//    private final StudentRepository studentRepository;
//
//    public Lecture findOne(Long id){
//        return lectureRepository.findOne(id);
//    }
//
//    public List<Lecture> findAll() {
//        return lectureRepository.findAll();
//    }
//
////  =========================== Instructor에서 요청하는 Service =========================
//    @Transactional
//    public Long create(Instructor instructor, AddLectureDto addLectureDto) {
//        Lecture lecture = Lecture.createLecture(addLectureDto.getName(), instructor, addLectureDto.getPersonnel(), addLectureDto.getLectureStatus());
//        if (addLectureDto.getImageFile() != null && !addLectureDto.getImageFile().isEmpty()) {
//            try {
//                saveLectureImageFile(addLectureDto.getImageFile(), lecture);
//            } catch (IOException e) {
//                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
//            }
//        }
//
//        lectureRepository.save(lecture);
//        instructor.addLectures(lecture);
//        return lecture.getId();
//    }
//
//    public List<Lecture> findAllLectures(Instructor instructor) {
//        List<Lecture> lectures = instructor.getLectures().values().stream().toList();
//        return lectures;
//    }
//
//    public List<Lecture> findByPageMyLectures(Instructor instructor, int page, int pageSize) {
//        List<Lecture> lectures = instructorRepository.findByPageMyLectures(instructor.getId(), page, pageSize);
//        return lectures;
//    }
//
//    @Transactional
//    public void updateLecture(Instructor instructor, UpdateLectureDto updateLectureDto, int updateRemainingPersonnel) {
//        Lecture lecture = instructor.getLectures().get(updateLectureDto.getLectureId());
//
//        lecture.setPersonnel(updateLectureDto.getPersonnel());
//        lecture.setRemainingPersonnel(updateRemainingPersonnel);
//        lecture.setLectureStatus(updateLectureDto.getLectureStatus());
//
//        if (updateLectureDto.getImageFile() != null && !updateLectureDto.getImageFile().isEmpty()) {
//            try {
//                saveLectureImageFile(updateLectureDto.getImageFile(), lecture);
//            } catch (IOException e) {
//                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
//            }
//        }
//    }
//
////    ========================= Student에서 요청하는 Service ============================
//    @Transactional
//    public Long applyLecture(Student student, Long lectureId) {
//        Lecture lecture = findOne(lectureId);
//
//        LectureStudentMapper mapper = new LectureStudentMapper();
//        mapperRepository.save(mapper);
//
//        student.applyLecture(mapper, lecture);
//        return lectureId;
//    }
//
//    @Transactional
//    public void cancelLecture(Student student, Long lectureId) {
//        // 엔티티 조회
//        Lecture lecture = lectureRepository.findOne(lectureId);
//
//        // mapper 제거
//        LectureStudentMapper mapper = student.getApplyingLectures().get(lectureId);
//        mapperRepository.cancel(mapper);
//
//        student.cancelLecture(lecture);
//    }
//
//    public List<Lecture> findAllLectures(Student student) {
//        List<Lecture> lectures = student.findAllLectures();
//        return lectures;
//    }
//
//    public List<Lecture> findPersonalizedLectures(SearchConditionDto searchConditionDto) {
//        String status = searchConditionDto.getStatus();
//        String condition = searchConditionDto.getCondition();
//        String text = searchConditionDto.getText();
//
//        if (status != null && !status.isBlank() && !status.equals("ALL")) {
//            if (condition != null && !condition.isBlank()) {
//                return findByAllConditions(condition, text, status);
//            }
//            return findByLectureStatus(status);
//        }
//
//        if (condition != null && !condition.isBlank()) {
//            return findByCondition(condition, text);
//        }
//
//        return findAll();
//    }
//
//    public List<Lecture> findByPageMyLectures(Student student, int page, int pageSize) {
//        List<Lecture> lectures = studentRepository.findByPageMyLectures(student.getId(), page, pageSize);
//        return lectures;
//    }
//
//    private List<Lecture> findByCondition(String condition, String text) {
//        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);
//
//        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
//            List<Lecture> findByInstructorList = findByInstructorName(text);
//            return findByInstructorList;
//        }
//
//        List<Lecture> findByLectureList = findByName(text);
//        return findByLectureList;
//    }
//
//    public List<Lecture> findByName(String name) {
//        return lectureRepository.findByName(name);
//    }
//
//    public List<Lecture> findByInstructorName(String instructorName) {
//        return lectureRepository.findByInstructorName(instructorName);
//    }
//
//    public List<Lecture> findByLectureStatus(LectureStatus lectureStatus) {
//        return lectureRepository.findByLectureStatus(lectureStatus);
//    }
//
//    private List<Lecture> findByAllConditions(String condition, String text, String status) {
//        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);
//        LectureStatus lectureStatus = LectureStatus.valueOf(status);
//
//        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
//            List<Lecture> allConditionLectures = lectureRepository.findByLectureStatusByInstructorName(lectureStatus, text);
//            return allConditionLectures;
//        }
//
//        List<Lecture> allConditionLectures = lectureRepository.findByLectureStatusByName(lectureStatus, text);
//        return allConditionLectures;
//    }
//
//    private List<Lecture> findByLectureStatus(String status) {
//        LectureStatus lectureStatus = LectureStatus.valueOf(status);
//        List<Lecture> findByLectureStatusList = findByLectureStatus(lectureStatus);
//        return findByLectureStatusList;
//    }
//}