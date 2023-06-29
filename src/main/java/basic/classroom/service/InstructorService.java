package basic.classroom.service;

import basic.classroom.dto.*;
import basic.classroom.domain.*;
import basic.classroom.repository.InstructorRepository;
import basic.classroom.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            log.info("duplicated!");
            throw new IllegalStateException();
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
    public void updateLecture(Long instructorId, UpdateLectureDto updateLectureDto) {
        Instructor instructor = instructorRepository.findOne(instructorId);
        Lecture lecture = instructor.getLectures().get(updateLectureDto.getLectureId());

        log.info("instructor lecture={}", instructor.getLectures());
        log.info("instructor lecture={}", instructor.getLectures().values());

        lecture.setPersonnel(updateLectureDto.getPersonnel());
        lecture.setLectureStatus(updateLectureDto.getLectureStatus());
    }

    public List<Lecture> findLectures(Long id) {
        List<Lecture> lectures = instructorRepository.findOne(id).getLectures().values().stream().toList();
        return lectures;
    }
}
