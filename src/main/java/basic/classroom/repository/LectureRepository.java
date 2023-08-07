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

    public List<Lecture> findByPage(int page, int pageSize) {
        return em.createQuery("select l from Lecture l", Lecture.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    public List<Lecture> findByPageByLectureStatus(int page, int pageSize, LectureStatus lectureStatus) {
        return em.createQuery("select l from Lecture l where l.lectureStatus = :lectureStatus", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Lecture> findByPageByName(int page, int pageSize, String name) {
        return em.createQuery("select l from Lecture l where l.name = :name", Lecture.class)
                .setParameter("name", name)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Lecture> findByPageByInstructorName(int page, int pageSize, String instructorName) {
        return em.createQuery("select l from Lecture l where l.instructor.member.name = :instructorName", Lecture.class)
                .setParameter("instructorName", instructorName)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Lecture> findByPageByLectureStatusByName(int page, int pageSize, LectureStatus lectureStatus, String name) {
        return em.createQuery("select l from Lecture l " +
                        "where l.lectureStatus = :lectureStatus and l.name = :name", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .setParameter("name", name)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Lecture> findByPageByLectureStatusByInstructorName(int page, int pageSize, LectureStatus lectureStatus, String instructorName) {
        return em.createQuery("select l from Lecture l " +
                        "where l.lectureStatus = :lectureStatus and l.instructor.member.name = :instructorName", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .setParameter("instructorName", instructorName)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Lecture> findByLectureStatusByInstructorName(LectureStatus lectureStatus, String instructorName) {
        return em.createQuery("select l from Lecture l " +
                        "where l.lectureStatus = :lectureStatus and l.instructor.member.name = :instructorName", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .setParameter("instructorName", instructorName)
                .getResultList();
    }
    public List<Lecture> findByLectureStatusByName(LectureStatus lectureStatus, String name) {
        return em.createQuery("select l from Lecture l " +
                        "where l.lectureStatus = :lectureStatus and l.name = :name", Lecture.class)
                .setParameter("lectureStatus", lectureStatus)
                .setParameter("name", name)
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
