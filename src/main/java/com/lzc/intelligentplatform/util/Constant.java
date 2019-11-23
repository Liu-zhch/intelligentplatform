package com.lzc.intelligentplatform.util;

public interface Constant {
    interface ResponseCode {
        Integer SUCCESS = 0;
        Integer FAILED = 1000;
    }

    interface ResponseMessage {
        String SUCCESS = "success";
    }
    interface Sex{
        Integer MAN = 1;
        Integer WAMAN = 2;
    }
    interface RoleType {
        Integer LEVEL1 = 1;
        Integer LEVEL2 = 2;
        Integer LEVEL3 = 3;
        Integer LEVEL4 = 4;
    }
    interface Reg{
        String cellPhone = "^1[3|4|5|7|8][0-9]\\\\d{4,8}$";
        String ident = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
    }
    String DEFAULT_ADMIN_NAME = "admin";
    String DEFAULT_ADMIN_PASSWD = "admin";
}
