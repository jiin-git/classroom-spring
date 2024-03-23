package basic.classroom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// Mapper의 역할? ManyToMany의 관계를 one to many - many to one 으로 변경해주기 위함
// student / lecture에서 조회 시 mapper에 연결된 lecture / student를 조회할 수 있게 변경?
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureStudentMapper {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    @JsonIgnore
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;

    public Long getStudentId() {
        return student.getId();
    }
    public void addStudent(Student student) {
        this.student = student;
    }

    public Long getLectureId() {
        return lecture.getId();
    }
    public void addLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
