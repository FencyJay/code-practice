package com.codebear.coderpracticebackend.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> records;

    private Long total;

    private Integer current;

    private Integer pageSize;

    private Integer pages;

    public PageResult(List<T> records, Long total, Integer current, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }
}