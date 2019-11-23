package com.lzc.intelligentplatform.request;

import lombok.Data;

@Data
public class AdminListRequest extends RequestPage {
    private String match;
    /**
     * 支持指定字段检索
     */
    private String property;
}
