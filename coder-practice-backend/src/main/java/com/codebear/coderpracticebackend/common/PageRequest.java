package com.codebear.coderpracticebackend.common;

import lombok.Data;

@Data
public class PageRequest {

    private Integer current = 1;

    private Integer pageSize = 10;

    public Integer getCurrent() {
        return current != null && current > 0 ? current : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}