package basic.classroom.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class SearchLectureRequest {
    private String status;
    private String condition;
    private String text;
    private Long page;

    protected SearchLectureRequest() {
        this.page = 1L;
    }
}
