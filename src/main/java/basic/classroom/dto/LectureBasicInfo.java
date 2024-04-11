package basic.classroom.dto;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LectureBasicInfo {
    private Long id;
    private String name;
    private int personnel;
    private int remainingPersonnel;
    private LectureStatus lectureStatus;

    public static LectureBasicInfo fromLecture(Lecture lecture) {
        return LectureBasicInfo.builder()
                .id(lecture.getId())
                .name(lecture.getName())
                .personnel(lecture.getPersonnel())
                .remainingPersonnel(lecture.getRemainingPersonnel())
                .lectureStatus(lecture.getLectureStatus()).build();
    }
}
