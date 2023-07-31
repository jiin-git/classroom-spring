package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Instructor {

    @Id @GeneratedValue
    @Column(name = "instructor_id")
    private Long id;

    @Embedded
    private Member member;

    @Embedded
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private Map<Long, Lecture> lectures = new HashMap();

    public Instructor(Member member) {
        this.member = member;
    }

    // 1:n 관계
    public void addLectures(Lecture lecture) {
        Long lectureId = lecture.getId();
        lectures.put(lectureId, lecture);
        lecture.setInstructor(this);
    }

    /* 생성 메서드 */
    public static Instructor createInstructor(Member member, Lecture... lectures) {
        Instructor instructor = new Instructor();
        instructor.setMember(member);
        for (Lecture lecture : lectures) {
            instructor.addLectures(lecture);
        }

        return instructor;
    }
}
