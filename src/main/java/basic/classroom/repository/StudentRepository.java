package basic.classroom.repository;

import basic.classroom.domain.Student;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final EntityManager em;
    
    public void save(Student student) {
        em.persist(student);
    }

    public Student findOne(Long id) {
        return em.find(Student.class, id);
    }

    public List<Student> findAll() {
        return em.createQuery("select s from Student s", Student.class)
                .getResultList();
    }

    public Optional<Student> findByLoginId(String loginId) {
        return em.createQuery("select s from Student s where s.member.loginId =: loginId", Student.class)
                .setParameter("loginId", loginId)
                .getResultList().stream().findFirst();
    }
    
    public List<Student> findByName(String name) {
        return em.createQuery("select s from Student s where s.member.name =: name", Student.class)
                .setParameter("name", name)
                .getResultList();
    }

}
