package basic.classroom.repository.dataJpa;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMember_LoginId(String loginId);
    Optional<Student> findByMember_LoginIdAndMember_Email(String loginId, String email);

    //    @EntityGraph
    @Query(value = "select al.lecture from Student s join s.applyingLectures al where s.id = :id",
            countQuery = "select count(al) from LectureStudentMapper al where al.student.id = :id")
    Page<Lecture> findLecturesByApplyingLectures_Student_Id(@Param("id") Long studentId, Pageable pageable);

    @Query("select s.member.loginId from Student s where s.member.name = :name and s.member.email = :email")
    List<String> findLoginIdsByMember_NameAndMember_Email(@Param("name") String name, @Param("email") String email);
}
