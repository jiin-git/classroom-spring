package basic.classroom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
public class SearchConditionDto {
    private String status;
    private String condition;
    private String text;
    private Integer page;
    private Pageable pageable;

    public SearchConditionDto(String status, String condition, String text, Integer page) {
        this.status = status;
        this.condition = condition;
        this.text = text;
        this.page = page;
    }
    public SearchConditionDto(String status, String condition, String text, Pageable pageable) {
        this.status = status;
        this.condition = condition;
        this.text = text;
        this.pageable = pageable;
    }
}
