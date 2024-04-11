package basic.classroom.domain;

import basic.classroom.exception.ErrorCode;
import basic.classroom.exception.LectureNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
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

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> applyingLectures;

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

    public LectureStudentMapper getMapper(Long mapperId) {
        boolean containsKey = applyingLectures.containsKey(mapperId);
        if (!containsKey) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }
        return applyingLectures.get(mapperId);
    }
    public Map<Long, Long> getAllMyLectureIds() {
        Map<Long, Long> lectureIds = new HashMap<>();
        applyingLectures.forEach((key, mapper) -> lectureIds.put(mapper.getLecture().getId(), key));
        return lectureIds;
    }

    public void applyLecture(LectureStudentMapper mapper, Lecture lecture) {
        Long mapperId = mapper.getId();
        applyingLectures.put(mapperId, mapper);

        mapper.addStudent(this);
        lecture.addStudent(mapper);
    }
    public Lecture getLecture(Long mapperId) {
        boolean containsKey = applyingLectures.containsKey(mapperId);
        if (!containsKey) {
            throw new LectureNotFoundException(ErrorCode.LECTURE_NOT_FOUND);
        }

        return applyingLectures.get(mapperId).getLecture();
    }
    public void cancelLecture(Long mapperId) {
        Lecture lecture = getLecture(mapperId);
        applyingLectures.remove(mapperId);
        lecture.removeStudent(mapperId);
    }
}
