package basic.classroom.repository.jpa;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstructorRepository {
    private final EntityManager em;

    public void save(Instructor instructor) {
        em.persist(instructor);
    }

    public Instructor findOne(Long id) {
        return em.find(Instructor.class, id);
    }

    public List<Instructor> findAll() {
        return em.createQuery("select i from Instructor i", Instructor.class)
                .getResultList();
    }

    public List<Lecture> findByPageMyLectures(Long id, int page, int pageSize) {
        return em.createQuery("select i.lectures from Instructor i where i.id = :id", Lecture.class)
                .setParameter("id", id)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Optional<Instructor> findByLoginId(String loginId) {
        return em.createQuery("select i from Instructor i where i.member.loginId =: loginId", Instructor.class)
                .setParameter("loginId", loginId)
                .getResultList().stream().findFirst();
    }

    public List<Instructor> findByName(String name) {
        return em.createQuery("select i from Instructor i where i.member.name =: name", Instructor.class)
                .setParameter("name", name)
                .getResultList();
    }

}
