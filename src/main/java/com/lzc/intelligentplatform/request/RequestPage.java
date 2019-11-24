package com.lzc.intelligentplatform.request;

import lombok.Data;

@Data
public class RequestPage {
    private Integer pageSize;
    private Integer pageNum;
    private Long total;
}
