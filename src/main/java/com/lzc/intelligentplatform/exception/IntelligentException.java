package com.lzc.intelligentplatform.exception;

import com.lzc.intelligentplatform.util.Constant;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class IntelligentException extends RuntimeException {

    private static final long serialVersionUID = 194906846739586856L;

    protected Integer code = Constant.ResponseCode.FAILED;

    protected String message;

    protected Object data;

    public IntelligentException() {

    }

    public IntelligentException(String message) {
        this();
        this.message = message;
    }

    public IntelligentException(String format, Object... objects) {
        this();
        format = StringUtils.replace(format, "{}", "%s");
        this.message = String.format(format, objects);
    }

    public IntelligentException(String msg, Throwable cause, Object... objects) {
        this();
        String format = StringUtils.replace(msg, "{}", "%s");
        this.message = String.format(format, objects);
    }


}



