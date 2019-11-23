package com.lzc.intelligentplatform.response;

import com.lzc.intelligentplatform.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private Integer code = Constant.ResponseCode.SUCCESS;
    private String message = Constant.ResponseMessage.SUCCESS;
    private Object data;
}
