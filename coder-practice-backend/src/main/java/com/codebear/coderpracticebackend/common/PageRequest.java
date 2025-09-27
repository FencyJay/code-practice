package com.codebear.coderpracticebackend.common;

import lombok.Data;

@Data
public class PageRequest {

    private Integer current = 1;

    private Integer pageSize = 10;

    public Integer getCurrent() {
        // 页码必须大于0，且最大不超过1000（防止过大页码攻击）
        if (current == null || current <= 0) {
            return 1;
        }
        return Math.min(current, 1000);
    }

    public Integer getPageSize() {
        // 每页大小必须在1-100之间
        if (pageSize == null || pageSize <= 0) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }

    /**
     * 获取分页偏移量
     * @return 偏移量
     */
    public Integer getOffset() {
        return (getCurrent() - 1) * getPageSize();
    }

    /**
     * 校验分页参数是否有效
     * @return 是否有效
     */
    public boolean isValid() {
        return current != null && current > 0 && pageSize != null && pageSize > 0;
    }
}