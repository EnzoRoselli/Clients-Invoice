package com.udemy.data.jpa.util.paginator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageItem {
    private Integer num;
    private boolean actual;
}
