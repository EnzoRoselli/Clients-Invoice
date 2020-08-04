package com.udemy.data.jpa.util.paginator;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRender<T> {
    private String url;
    private Page<T> page;
    private Integer totalPages;
    private Integer elementsPerPage;
    private Integer actualPage;
    private List<PageItem> pages;

    public PageRender(String url, Page<T> page){
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<>();

        elementsPerPage = page.getSize();
        totalPages = page.getTotalPages();
        actualPage = page.getNumber() + 1;

        Integer from, until;
        if (totalPages <= elementsPerPage){
            from = 1;
            until = totalPages;
        }else{
            if (actualPage <= elementsPerPage/2){
                from=1;
                until=elementsPerPage;
            }else if (actualPage >= totalPages - elementsPerPage/2){
                from=totalPages - elementsPerPage + 1;
                until= elementsPerPage;
            }else {
                from = actualPage - elementsPerPage/2;
                until= elementsPerPage;
            }
        }

        for (int i=0; i < until; i++){
            pages.add(new PageItem(from+i, actualPage == from+i));
        }
    }

    public boolean isFirst(){
        return page.isFirst();
    }

    public boolean isLast(){
        return page.isLast();
    }

    public boolean isHasNext(){
        return page.hasNext();
    }

    public boolean isHasPrevious(){
        return page.hasPrevious();
    }
}
