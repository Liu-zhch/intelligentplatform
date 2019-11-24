package com.lzc.intelligentplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzc.intelligentplatform.entity.AdminUser;
import com.lzc.intelligentplatform.exception.IntelligentException;
import com.lzc.intelligentplatform.request.AdminListRequest;
import com.lzc.intelligentplatform.response.ResponseMessage;
import com.lzc.intelligentplatform.service.AdminUserService;
import com.lzc.intelligentplatform.util.ExceptionHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage addAdminUser(@RequestBody @Validated AdminUser user, HttpServletRequest request) {
        ResponseMessage message = new ResponseMessage();
        HttpSession session = request.getSession();
        try {
            log.info("add user:{}", JSONObject.toJSONString(user));
            userService.addAdminUser(user, session);
            return message;
        } catch (Exception e) {
            ExceptionHandle.handle(e, message);
        }
        return message;
    }

    @RequestMapping(value = "/getAdminUser", method = RequestMethod.GET)
    public ResponseMessage getAdminUser(AdminListRequest request, HttpServletRequest req) {
        ResponseMessage message = new ResponseMessage();
        HttpSession session = req.getSession();
        try {
            log.info("user query:{}", JSONObject.toJSONString(request));
            message.setData(userService.getAdminUser(request, session));
            return message;
        } catch (Exception e) {
            ExceptionHandle.handle(e, message);
        }
        return message;
    }

    @RequestMapping(value = "/delAdminUser", method = RequestMethod.POST)
    public ResponseMessage getAdminUser(@RequestBody String ids, HttpServletRequest req) {
        ResponseMessage message = new ResponseMessage();
        HttpSession session = req.getSession();
        try {
            JSONObject object = JSONObject.parseObject(ids);
            if (object.getString("ids") == null) {
                throw new IntelligentException("参数错误");
            }
            List<String> idList = Arrays.asList(object.getString("ids").split(","));
            Set<String> idSet = new HashSet<>();
            idSet.addAll(idList);
            log.info("del:{}", JSONObject.toJSONString(idSet));
            userService.delAdminUser(idSet, session);
            return message;
        } catch (Exception e) {
            ExceptionHandle.handle(e, message);
        }
        return message;
    }
}
