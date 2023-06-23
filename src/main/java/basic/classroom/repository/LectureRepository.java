package basic.classroom.repository;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureRepository {

    private final EntityManager em;

    public void save(Lecture lecture) {
        em.persist(lecture);
    }

    public Lecture findOne(Long id) {
        return em.find(Lecture.class, id);
    }

    public List<Lecture> findAll() {
        return em.createQuery("select l from Lecture l", Lecture.class)
                .getResultList();
    }

    public List<Lecture> findByName(String name) {
        return em.createQuery("select l from Lecture l where l.name =: name", Lecture.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Lecture> findByInstructorName(String instructorName) {
        return em.createQuery("select l from Lecture l where l.instructor.member.name =: instructorName", Lecture.class)
                .setParameter("instructorName", instructorName)
                .getResultList();
    }

    public List<Lecture> findByLectureStatus(LectureStatus lectureStatus) {
        return em.createQuery("select l from Lecture l where l.lectureStatus =: lectureStatus", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .getResultList();
    }

}
