package basic.classroom.domain;

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

    @Embedded
    private ProfileImage profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private int personnel;
    private int remainingPersonnel;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> appliedStudents = new HashMap<>();

    // 1:n - lecture : appliedStudent
    public void addStudent(LectureStudentMapper appliedStudent) {
        Long studentId = appliedStudent.getStudent().getId();
        getAppliedStudents().put(studentId, appliedStudent);

        // 인원 초과시 에러
        if (remainingPersonnel <= 0) {
            throw new IllegalStateException();
        }

        setRemainingPersonnel(remainingPersonnel - 1);
        // 정원이 다 차면 FULL
        if (remainingPersonnel == 0) {
            setLectureStatus(LectureStatus.FULL);
        }
    }

    /* 생성 메서드 */
    public static Lecture createLecture(String name, Instructor instructor, int personnel, LectureStatus lectureStatus, LectureStudentMapper... appliedStudents) {
        Lecture lecture = new Lecture();
        lecture.setName(name);
        lecture.setInstructor(instructor);
        lecture.setPersonnel(personnel);
        lecture.setRemainingPersonnel(personnel);
        lecture.setLectureStatus(lectureStatus);
        for (LectureStudentMapper appliedStudent : appliedStudents) {
            lecture.addStudent(appliedStudent);
        }

        return lecture;
    }

    /* 수강 신청 취소 */
    public void removeStudent(Long appliedStudentId) {
        appliedStudents.remove(appliedStudentId);
        setRemainingPersonnel(remainingPersonnel + 1);

        if (lectureStatus == LectureStatus.FULL) {
            setLectureStatus(LectureStatus.OPEN);
        }
    }

    /* 학생 명단 조회 */
    public List<Student> findAppliedStudents() {
        List<Student> students = new ArrayList<>();
        appliedStudents.forEach((k, m) -> students.add(m.getStudent()));

        return students;
    }
}
