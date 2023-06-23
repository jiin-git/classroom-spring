package basic.classroom.dto;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.LectureStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
@NoArgsConstructor
public class UpdateLectureDto {

    @NotNull
    private Long lectureId;
    private String lectureName;
    private Long instructorId;
    private String instructorName;

    @Range(min = 1, max = 100, message = "정원을 1 ~ 100명 사이로 입력해주세요.")
    private int personnel;

    @NotNull(message = "강의 상태를 설정해주세요.")
    private LectureStatus lectureStatus;

    public UpdateLectureDto(Lecture lecture) {
        this.lectureId = lecture.getId();
        this.lectureName = lecture.getName();
        this.instructorId = lecture.getInstructor().getId();
        this.instructorName = lecture.getInstructor().getMember().getName();
        this.personnel = lecture.getPersonnel();
        this.lectureStatus = lecture.getLectureStatus();
    }
}
