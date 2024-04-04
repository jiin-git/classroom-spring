package basic.classroom.domain;

import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.LectureNotFoundException;
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
@Getter
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

    // key : lecture id -> mapper id 변경(mapper를 통해서만 강의 엔티티에 접근할 수 있다)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> applyingLectures;

    // applyingLectures - key: lectureId -> mapperId 변경
    public void applyLecture(LectureStudentMapper mapper, Lecture lecture) {
        Long mapperId = mapper.getId();
        applyingLectures.put(mapperId, mapper);

        mapper.addStudent(this);
        lecture.addStudent(mapper);
    }
    public void cancelLecture(Lecture lecture) {
        applyingLectures.remove(lecture.getId());
        lecture.removeStudent(id);
    } /* 삭제 예정 */
    public void cancelLecture(Long mapperId, Lecture lecture) {
        applyingLectures.remove(mapperId);
        lecture.removeStudent(mapperId);
    }

    public LectureStudentMapper getMapper(Long mapperId) {
        boolean containsKey = applyingLectures.containsKey(mapperId);
        if (!containsKey) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }
        return applyingLectures.get(mapperId);
    }
    public Lecture getLecture(Long mapperId) {
        log.info("mapper Id = {}", mapperId);
        boolean containsKey = applyingLectures.containsKey(mapperId);
        if (!containsKey) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }

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
    public void updateStudent(ProfileImage profileImage) {
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
}
