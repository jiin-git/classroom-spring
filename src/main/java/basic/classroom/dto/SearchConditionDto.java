package basic.classroom.dto;

import basic.classroom.domain.LectureSearchCondition;
import basic.classroom.domain.LectureStatusSearchCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchConditionDto {
    private String status;
    private String condition;
    private String text;

    public SearchConditionDto(String status, String condition, String text) {
        this.status = status;
        this.condition = condition;
        this.text = text;
    }
}
