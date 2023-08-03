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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static basic.classroom.service.ProfileImageService.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final LectureRepository lectureRepository;

// === lecture Service ====
    @Transactional
    public void updateLecture(Long instructorId, UpdateLectureDto updateLectureDto, int updateRemainingPersonnel) {
        Instructor instructor = instructorRepository.findOne(instructorId);
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

    public List<Lecture> findLectures(Long id) {
        List<Lecture> lectures = instructorRepository.findOne(id).getLectures().values().stream().toList();
        return lectures;
    }
}
