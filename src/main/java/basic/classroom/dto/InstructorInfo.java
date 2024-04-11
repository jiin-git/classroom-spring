package basic.classroom.dto;

import basic.classroom.domain.Lecture;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InstructorInfo {
    private Long id;
    private String name;

    public static InstructorInfo fromLecture(Lecture lecture) {
        return InstructorInfo.builder()
                .id(lecture.getInstructor().getId())
                .name(lecture.getInstructor().getMember().getName()).build();
    }
}
