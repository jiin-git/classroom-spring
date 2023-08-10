package basic.classroom.repository.dataJpa;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByName(String name);
    List<Lecture> findByLectureStatus(LectureStatus lectureStatus);
    List<Lecture> findByInstructor_Member_Name(String instructorName);
    List<Lecture> findByLectureStatusAndName(LectureStatus lectureStatus, String name);
    List<Lecture> findByLectureStatusAndInstructor_Member_Name(LectureStatus lectureStatus, String instructorName);

    Page<Lecture> findByName(String name, Pageable pageable);
    Page<Lecture> findByLectureStatus(LectureStatus lectureStatus, Pageable pageable);
    Page<Lecture> findByInstructor_Member_Name(String instructorName, Pageable pageable);
    Page<Lecture> findByLectureStatusAndName(LectureStatus lectureStatus, String name, Pageable pageable);
    Page<Lecture> findByLectureStatusAndInstructor_Member_Name(LectureStatus lectureStatus, String instructorName, Pageable pageable);

}