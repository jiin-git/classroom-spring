package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    @Id @GeneratedValue
    @Column(name = "instructor_id")
    private Long id;

    @Embedded
    private Member member;

    @Embedded
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
//    @MapKey(name = "id")
    private Map<Long, Lecture> lectures;
    public void addLectures(Lecture lecture) {
        log.info("lecture id = {}, lecture name = {}", lecture.getId(), lecture.getName());
        lectures.put(lecture.getId(), lecture);
        lecture.addInstructor(this);
    }
    public Instructor(Member member) {
        this.member = member;
    } // 삭제 예정
    public static Instructor createInstructor(Member member) {
        return Instructor.builder().member(member).lectures(new HashMap<>()).build();
    }
    public void updateInstructor(String email, ProfileImage profileImage) {
        this.member.updateEmail(email);
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }
    public void updateInstructorPassword(String password) {
        this.member.updatePassword(password);
    }
}
