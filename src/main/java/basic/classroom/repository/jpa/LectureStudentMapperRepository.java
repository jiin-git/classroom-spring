package basic.classroom.repository.jpa;

import basic.classroom.domain.LectureStudentMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureStudentMapperRepository {

    private final EntityManager em;

    public void save(LectureStudentMapper mapper) {
        em.persist(mapper);
    }

    public void cancel(LectureStudentMapper mapper) {
        em.remove(mapper);
    }

    public LectureStudentMapper findOne(Long id) {
        return em.find(LectureStudentMapper.class, id);
    }
}
