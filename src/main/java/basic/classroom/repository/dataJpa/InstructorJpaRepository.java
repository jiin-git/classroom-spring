package basic.classroom.repository.dataJpa;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstructorJpaRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByMember_LoginId(String loginId);
    Optional<Instructor> findByMember_LoginIdAndMember_Email(String loginId, String email);

    @Query(value = "select i.lectures from Instructor i where i.id = :id",
            countQuery = "select count(l) from Lecture l where l.instructor.id = :id")
    Page<Lecture> findLecturesById(@Param("id") Long id, Pageable pageable);

    @Query("select i.member.loginId from Instructor i where i.member.name = :name and i.member.email = :email")
    List<String> findLoginIdsByMember_NameAndMember_Email(@Param("name") String name, @Param("email") String email);
}
