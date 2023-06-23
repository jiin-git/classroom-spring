package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Entity
@Getter @Setter
public class Student {

    @Id @GeneratedValue
    @Column(name = "student_id")
    private Long id;

    @Embedded
    private Member member;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> applyingLectures = new HashMap<>();

    // 1:n
    /* 강의 신청 메서드 */
    public void applyLecture(LectureStudentMapper applyingLecture) {
        Long lectureId = applyingLecture.getLecture().getId();
        getApplyingLectures().put(lectureId, applyingLecture);
    }

    /* 생성 메서드 */
    public static Student createStudent(Member member, LectureStudentMapper... applyLectures) {
        Student student = new Student();
        student.setMember(member);
        for (LectureStudentMapper applyLecture : applyLectures) {
            student.applyLecture(applyLecture);
        }

        return student;
    }

    /* 강의 취소 메서드 */
    public void cancelLecture(Long applyingLectureId) {
        applyingLectures.remove(applyingLectureId);
    }


    /* 전체 강의 조회 */
    public List<Lecture> findAllLectures() {
        List<Lecture> lectures = new ArrayList<>();
        applyingLectures.forEach((k, m) -> lectures.add(m.getLecture()));

        return lectures;
    }

}
