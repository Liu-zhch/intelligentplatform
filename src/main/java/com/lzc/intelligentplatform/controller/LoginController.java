package com.lzc.intelligentplatform.controller;

import com.lzc.intelligentplatform.entity.AdminUser;
import com.lzc.intelligentplatform.response.ResponseMessage;
import com.lzc.intelligentplatform.service.AdminUserService;
import com.lzc.intelligentplatform.util.Constant;
import com.lzc.intelligentplatform.util.ExceptionHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("login")
    public ResponseMessage login(String adminName, String password, HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage();
        try {
            HttpSession session = request.getSession();
            AdminUser user = adminUserService.getAdminUser(adminName);
            if (user == null) {
                message.setCode(Constant.ResponseCode.FAILED);
                message.setMessage("账号不存在");
                return message;
            }
            if (password.equals(user.getPassword())) {
                message.setMessage("登录成功");
                message.setData(user);
                session.setAttribute(Constant.USER_SESSION_KEY, user);
                return message;
            }
            message.setCode(Constant.ResponseCode.FAILED);
            message.setMessage("账号不存在");
            return message;
        } catch (Exception e) {
            ExceptionHandle.handle(e, message);
            return message;
        }
    }
}
