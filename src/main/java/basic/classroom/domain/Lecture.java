package basic.classroom.domain;

import basic.classroom.dto.AddLectureDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Lecture {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private int personnel;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> appliedStudents = new HashMap<>();

    // 1:n - lecture : appliedStudent
    public void addStudent(LectureStudentMapper appliedStudent) {
        Long studentId = appliedStudent.getStudent().getId();
        getAppliedStudents().put(studentId, appliedStudent);

        // 인원 초과시 에러
        if (personnel <= 0) {
            throw new IllegalStateException();
        }

        setPersonnel(personnel - 1);
    }

    /* 생성 메서드 */
    public static Lecture createLecture(String name, Instructor instructor, int personnel, LectureStatus lectureStatus, LectureStudentMapper... appliedStudents) {
        Lecture lecture = new Lecture();
        lecture.setName(name);
        lecture.setInstructor(instructor);
        lecture.setPersonnel(personnel);
        lecture.setLectureStatus(lectureStatus);
        for (LectureStudentMapper appliedStudent : appliedStudents) {
            lecture.addStudent(appliedStudent);
        }

        return lecture;
    }

    /* 수강 신청 취소 */
    public void removeStudent(Long appliedStudentId) {
        appliedStudents.remove(appliedStudentId);
        setPersonnel(personnel + 1);
    }

    /* 학생 명단 조회 */
    public List<Student> findAppliedStudents() {
        List<Student> students = new ArrayList<>();
        appliedStudents.forEach((k, m) -> students.add(m.getStudent()));

        return students;
    }
}
