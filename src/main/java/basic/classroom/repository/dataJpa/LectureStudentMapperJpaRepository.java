package basic.classroom.repository.dataJpa;

import basic.classroom.domain.LectureStudentMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureStudentMapperJpaRepository extends JpaRepository<LectureStudentMapper, Long> {
}
