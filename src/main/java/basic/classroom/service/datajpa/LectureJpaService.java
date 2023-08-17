package basic.classroom.service.datajpa;

import basic.classroom.domain.*;
import basic.classroom.dto.AddLectureDto;
import basic.classroom.dto.SearchConditionDto;
import basic.classroom.dto.UpdateLectureDto;
import basic.classroom.exception.StoreImageException;
import basic.classroom.repository.dataJpa.InstructorJpaRepository;
import basic.classroom.repository.dataJpa.LectureJpaRepository;
import basic.classroom.repository.dataJpa.LectureStudentMapperJpaRepository;
import basic.classroom.repository.dataJpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static basic.classroom.service.ProfileImageService.saveLectureImageFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureJpaService {
    private final LectureJpaRepository lectureJpaRepository;
    private final LectureStudentMapperJpaRepository mapperJpaRepository;
    private final InstructorJpaRepository instructorJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    public Lecture findOne(Long id){
        return lectureJpaRepository.findById(id).get();
    }

    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll();
    }

//  =========================== Instructor에서 요청하는 Service =========================
    @Transactional
    public Long create(Instructor instructor, AddLectureDto addLectureDto) {
        Lecture lecture = Lecture.createLecture(addLectureDto.getName(), instructor, addLectureDto.getPersonnel(), addLectureDto.getLectureStatus());
        if (addLectureDto.getImageFile() != null && !addLectureDto.getImageFile().isEmpty()) {
            try {
                saveLectureImageFile(addLectureDto.getImageFile(), lecture);
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
        lectureJpaRepository.save(lecture);
        instructor.addLectures(lecture);
        return lecture.getId();
    }

    public List<Lecture> findAllLectures(Instructor instructor) {
        List<Lecture> lectures = instructor.getLectures().values().stream().toList();
        return lectures;
    }

    public Page<Lecture> findMyLecturesByPage(Instructor instructor, Pageable pageable) {
        Page<Lecture> lectures = instructorJpaRepository.findLecturesById(instructor.getId(), pageable);
        log.info("total pages = {}", lectures.getTotalPages());
        log.info("total elements = {}", lectures.getTotalElements());
        log.info("Content size = {}", lectures.getContent().size());
        return lectures;
    }

    public List<Student> findApplicantsById(Long id) {
        return lectureJpaRepository.findStudentsById(id);
    }

    @Transactional
    public void updateLecture(Instructor instructor, UpdateLectureDto updateLectureDto, int updateRemainingPersonnel) {
        Lecture lecture = instructor.getLectures().get(updateLectureDto.getLectureId());

        lecture.setPersonnel(updateLectureDto.getPersonnel());
        lecture.setRemainingPersonnel(updateRemainingPersonnel);
        lecture.setLectureStatus(updateLectureDto.getLectureStatus());

        if (updateLectureDto.getImageFile() != null && !updateLectureDto.getImageFile().isEmpty()) {
            try {
                saveLectureImageFile(updateLectureDto.getImageFile(), lecture);
            } catch (IOException e) {
                throw new StoreImageException("프로필 이미지를 저장할 수 없습니다. 이미지 형식과 사이즈를 다시 확인해주세요.", e);
            }
        }
    }

//    ========================= Student에서 요청하는 Service ============================

    @Transactional
    public Long applyLecture(Student student, Long lectureId) {
        Lecture lecture = findOne(lectureId);

        LectureStudentMapper mapper = new LectureStudentMapper();
        mapperJpaRepository.save(mapper);

        student.applyLecture(mapper, lecture);
        return lectureId;
    }

    @Transactional
    public void cancelLecture(Student student, Long lectureId) {
        // 엔티티 조회
        Lecture lecture = lectureJpaRepository.findById(lectureId).get();

        // mapper 제거
        LectureStudentMapper mapper = student.getApplyingLectures().get(lectureId);
        mapperJpaRepository.delete(mapper);

        student.cancelLecture(lecture);
    }

    public Page<Lecture> findMyLecturesByPage(Student student, Pageable pageable) {
        Page<Lecture> lectures = studentJpaRepository.findLecturesByApplyingLectures_Student_Id(student.getId(), pageable);

        return lectures;
    }

    public Page<Lecture> findPersonalizedLectures(SearchConditionDto searchConditionDto, int pageSize) {
        String status = searchConditionDto.getStatus();
        String condition = searchConditionDto.getCondition();
        String text = searchConditionDto.getText();
        Pageable pageable = PageRequest.of(searchConditionDto.getPage() - 1, pageSize);

        if (status != null && !status.isBlank() && !status.equals("ALL")) {
            LectureStatus lectureStatus = LectureStatus.valueOf(searchConditionDto.getStatus());
            if (condition != null && !condition.isBlank()) {
                return findByAllConditions(searchConditionDto, pageable);
            }
            return findByLectureStatus(lectureStatus, pageable);
        }

        if (condition != null && !condition.isBlank()) {
            return findByCondition(condition, text, pageable);
        }
        return findAll(pageable);
    }

    private Page<Lecture> findAll(Pageable pageable) {
        return lectureJpaRepository.findAll(pageable);
    }

    private Page<Lecture> findByAllConditions(SearchConditionDto searchConditionDto, Pageable pageable) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(searchConditionDto.getCondition());
        LectureStatus lectureStatus = LectureStatus.valueOf(searchConditionDto.getStatus());
        String text = searchConditionDto.getText();

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            return lectureJpaRepository.findByLectureStatusAndInstructor_Member_Name(lectureStatus, text, pageable);
        }

        return lectureJpaRepository.findByLectureStatusAndName(lectureStatus, text, pageable);
    }

    private Page<Lecture> findByLectureStatus(LectureStatus lectureStatus, Pageable pageable) {
        return lectureJpaRepository.findByLectureStatus(lectureStatus, pageable);
    }

    private Page<Lecture> findByCondition(String condition, String text, Pageable pageable) {
        LectureSearchCondition searchCondition = LectureSearchCondition.valueOf(condition);

        if (searchCondition == LectureSearchCondition.INSTRUCTOR) {
            return lectureJpaRepository.findByInstructor_Member_Name(text, pageable);
        }
        return lectureJpaRepository.findByName(text, pageable);
    }
}
