package basic.classroom.controller.api.pages;

import basic.classroom.service.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pages")
public class PagesApi {
    private final PagingService pagingService;

    @GetMapping("")
    public List<Integer> getShowPages(Pageable pageable, int totalPages) {
        return pagingService.getShowPages(pageable, totalPages);
    }
}
