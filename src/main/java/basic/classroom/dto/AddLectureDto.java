package basic.classroom.dto;

import basic.classroom.domain.LectureStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class AddLectureDto {

    @Size(min = 1, max = 15, message = "이름을 1 ~ 15자리 사이로 입력해주세요.")
    private String name;

    @Range(min = 1, max = 100, message = "정원을 1 ~ 100명 사이로 입력해주세요.")
    private int personnel;

    @NotNull(message = "강의 상태를 설정해주세요.")
    private LectureStatus lectureStatus;

    public AddLectureDto(String name, int personnel, LectureStatus lectureStatus) {
        this.name = name;
        this.personnel = personnel;
        this.lectureStatus = lectureStatus;
    }
}
