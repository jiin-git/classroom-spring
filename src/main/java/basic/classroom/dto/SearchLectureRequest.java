package basic.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchLectureRequest {
    private String status;
    private String condition;
    private String text;
    private Integer page;

    public SearchLectureRequest() {
        this.page = 1;
    }
}
