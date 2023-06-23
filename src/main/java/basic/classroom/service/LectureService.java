package basic.classroom.service;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.Student;
import basic.classroom.repository.LectureRepository;
import basic.classroom.repository.LectureStudentMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureStudentMapperRepository mapperRepository;

    @Transactional
    public Long create(Lecture lecture) {
        lectureRepository.save(lecture);
        return lecture.getId();
    }

    public Lecture findOne(Long id){
        return lectureRepository.findOne(id);
    }

    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public List<Lecture> findByName(String name) {
        return lectureRepository.findByName(name);
    }

    public List<Lecture> findByInstructorName(String instructorName) {
        return lectureRepository.findByInstructorName(instructorName);
    }

    public List<Lecture> findByLectureStatus(LectureStatus lectureStatus) {
        return lectureRepository.findByLectureStatus(lectureStatus);
    }

    public List<Student> findAllStudents(Long id) {
        Lecture lecture = lectureRepository.findOne(id);
        List<Student> students = lecture.findAppliedStudents();

        return students;
    }

}
