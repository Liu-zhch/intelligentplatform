package com.lzc.intelligentplatform.util;

import com.lzc.intelligentplatform.exception.IntelligentException;
import com.lzc.intelligentplatform.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandle {
    //private static Log log = LogFactory.getLog(ExceptionHandle.class);

    public static void handle(Exception e, ResponseMessage message) {
        message.setCode(Constant.ResponseCode.FAILED);
        if (e instanceof IntelligentException) {
            message.setMessage(e.getMessage());
            return;
        }
        log.info("exception:", e);
        message.setMessage("未知错误");
    }
}
