package basic.classroom.dto;

import basic.classroom.domain.LectureStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddLectureRequest {
    @Size(min = 1, max = 15, message = "이름을 1 ~ 15자리 사이로 입력해주세요.")
    private String name;
    @NotNull(message = "정원을 1 ~ 100명 사이로 입력해주세요.")
    @Range(min = 1, max = 100, message = "정원을 1 ~ 100명 사이로 입력해주세요.")
    private Long personnel;
    @NotNull(message = "강의 상태를 설정해주세요.")
    private LectureStatus lectureStatus;
    private MultipartFile imageFile;
}
