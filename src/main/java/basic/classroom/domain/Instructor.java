package basic.classroom.domain;

import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.LectureNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Entity
@Builder
@Getter
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
    private Map<Long, Lecture> lectures;

    public static Instructor createInstructor(Member member) {
        return Instructor.builder().member(member).lectures(new HashMap<>()).build();
    }

    public void updateInstructor(String email, ProfileImage profileImage) {
        member.updateEmail(email);
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }
    public void updatePassword(String password) {
        member.updatePassword(password);
    }
    public void clearProfileImage() {
        this.profileImage = null;
    }

    public void addLectures(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
        lecture.addInstructor(this);
    }
    public Lecture getLecture(Long lectureId) {
        boolean containsKey = lectures.containsKey(lectureId);
        if (!containsKey) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }
        return lectures.get(lectureId);
    }
    public List<Lecture> getAllLectures() {
        return lectures.values().stream().toList();
    }
}
