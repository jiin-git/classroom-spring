package basic.classroom.service;

import basic.classroom.domain.Lecture;
import basic.classroom.dto.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PagingService {
    public List<Lecture> filteringLectures(List<Lecture> lectures, int page) {
        List<Lecture> showLectures = new ArrayList<>();

        int pageUnit = 10;
        int startIdx = (page - 1) * pageUnit;
        int endIdx = startIdx;
        int lecturesCnt = lectures.size();
        int pageSize = getPageSize(lecturesCnt);

        if (page < pageSize) {
            endIdx += pageUnit;
        } else if (page == pageSize) {
            int showLectureCnt = lecturesCnt % pageUnit;
            if (showLectureCnt == 0 && lecturesCnt != 0) {
                endIdx += pageUnit;
            } else {
                endIdx += showLectureCnt;
            }
        }

        showLectures = lectures.subList(startIdx, endIdx);
        return showLectures;
    }

    public List<Integer> getShowPages(int lecturesCnt, int page) {
        int pageSize = getPageSize(lecturesCnt);
        int firstPage = 1;
        int endPage = pageSize;
        int showPageUnit = 3;

        List<Integer> pages = new ArrayList<>();
        List<Integer> showPages = new ArrayList<>();

        for (int i = 1; i <= pageSize; i++) {
            pages.add(i);
        }

        if (pageSize > showPageUnit) {
            if (page == firstPage) {
                showPages = pages.subList(firstPage - 1, showPageUnit);
            } else if (page == endPage) {
                showPages = pages.subList(endPage - showPageUnit, endPage);
            } else {
                showPages = pages.subList(page - 2, page + 1);
            }
        }
        else {
            showPages = pages;
        }

        return showPages;
    }

    public List<Integer> getShowPages(PageDto pageDto) {
        int page = pageDto.getPage();
        int startPage = pageDto.getStartPage();
        int endPage = pageDto.getEndPage();
        int showPageUnit = pageDto.getShowPageUnit();

        List<Integer> pages = new ArrayList<>();
        List<Integer> showPages = new ArrayList<>();

        for (int i = 1; i <= endPage; i++) {
            pages.add(i);
        }
        if (endPage > showPageUnit) {
            if (page == startPage) {
                showPages = pages.subList(startPage - 1, showPageUnit);
            } else if (page == endPage) {
                showPages = pages.subList(endPage - showPageUnit, endPage);
            } else {
                showPages = pages.subList(page - 2, page + 1);
            }
        }
        else {
            showPages = pages;
        }

        return showPages;
    }

    public List<Integer> getShowPages(Pageable pageable, int totalPages) {
        final int startPage = 1;
        final int showPageUnit = 3;

        int page = pageable.getPageNumber() + 1;
        int endPage = totalPages;

        List<Integer> pages = new ArrayList<>();
        List<Integer> showPages = new ArrayList<>();

        for (int i = 1; i <= endPage; i++) {
            pages.add(i);
        }
        if (endPage > showPageUnit) {
            if (page == startPage) {
                showPages = pages.subList(startPage - 1, showPageUnit);
            } else if (page == endPage) {
                showPages = pages.subList(endPage - showPageUnit, endPage);
            } else {
                showPages = pages.subList(page - 2, page + 1);
            }
        }
        else {
            showPages = pages;
        }

        return showPages;
    }

    public int getPageSize(int lecturesCnt) {
        if (lecturesCnt == 0) {
            return 1;
        }

        return (int) Math.ceil((double) lecturesCnt / 10);
    }
}
