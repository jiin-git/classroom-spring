package basic.classroom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse {
    private int page;
    private final int startPage = 1;
    private int endPage;
    private int showPageUnit;
    private int pageSize;

    public PageResponse(int page, int endPage, int showPageUnit, int pageSize) {
        this.page = page;
        this.endPage = endPage;
        this.showPageUnit = showPageUnit;
        this.pageSize = pageSize;
    }
}
