package basic.classroom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
    private int page;
    private final int startPage = 1;
    private int endPage;
    private int showPageUnit;
    private int pageSize;

    public PageDto(int page, int endPage, int showPageUnit, int pageSize) {
        this.page = page;
        this.endPage = endPage;
        this.showPageUnit = showPageUnit;
        this.pageSize = pageSize;
    }
}
