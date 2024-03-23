package basic.classroom.repository.dataJpa;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    Page<Lecture> findByName(String name, Pageable pageable);
    Page<Lecture> findByLectureStatus(LectureStatus lectureStatus, Pageable pageable);
    Page<Lecture> findByInstructor_Member_Name(String instructorName, Pageable pageable);
    Page<Lecture> findByLectureStatusAndName(LectureStatus lectureStatus, String name, Pageable pageable);
    Page<Lecture> findByLectureStatusAndInstructor_Member_Name(LectureStatus lectureStatus, String instructorName, Pageable pageable);

    @Query("select slist.student from Lecture l join l.appliedStudents slist where l.id = :id")
    List<Student> findStudentsById(@Param("id") Long id);
}
