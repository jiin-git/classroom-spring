package basic.classroom.dto;

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
    private Long page;
}
