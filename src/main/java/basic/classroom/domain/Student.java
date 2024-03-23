package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id @GeneratedValue
    @Column(name = "student_id")
    private Long id;

    @Embedded
    private Member member;

    @Embedded
    private ProfileImage profileImage;

    // key : lecture id -> mapper id 변경(mapper를 통해서만이 강의 엔티티에 접근할 수 있다)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> applyingLectures;

    public void applyLecture(LectureStudentMapper mapper, Lecture lecture) {
//        Long lectureId = lecture.getId();
//        applyingLectures.put(lectureId, mapper);
        Long mapperId = mapper.getId();
        applyingLectures.put(mapperId, mapper);

        mapper.addStudent(this);
        lecture.addStudent(mapper);
    }
    public void cancelLecture(Lecture lecture) {
        applyingLectures.remove(lecture.getId());
        lecture.removeStudent(id);
    }
    public Lecture getLecture(Long mapperId) {
        return applyingLectures.get(mapperId).getLecture();
    }
    public List<Lecture> findAllLectures() {
        List<Lecture> lectures = new ArrayList<>();
        applyingLectures.forEach((key, mapper) -> lectures.add(mapper.getLecture()));
        return lectures;
    }

    public static Student createStudent(Member member) {
        return Student.builder().member(member).applyingLectures(new HashMap<>()).build();
    }
    public void updateStudent(String email, ProfileImage profileImage) {
        member.updateEmail(email);
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }
    public void updateStudentPassword(String password) {
        member.updatePassword(password);
    }
}
