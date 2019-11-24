package com.lzc.intelligentplatform.service;

import com.alibaba.fastjson.JSONObject;
import com.lzc.intelligentplatform.entity.AdminUser;
import com.lzc.intelligentplatform.exception.IntelligentException;
import com.lzc.intelligentplatform.repository.AdminUserRepository;
import com.lzc.intelligentplatform.request.AdminListRequest;
import com.lzc.intelligentplatform.response.AdminListResponse;
import com.lzc.intelligentplatform.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private MongoTemplate template;

    @PostConstruct
    public void init() {
        AdminUser user = adminUserRepository.findFirstByRoleType(Constant.RoleType.LEVEL1);
        if (user == null) {
            user = new AdminUser();
            user.setAdminName(Constant.DEFAULT_ADMIN_NAME);
            user.setCreateTime(System.currentTimeMillis());
            user.setPassword(Constant.DEFAULT_ADMIN_PASSWD);
            user.setLastUpdateTime(user.getCreateTime());
            user.setRoleType(Constant.RoleType.LEVEL1);
            user.setSex(Constant.Sex.MAN);
            user.setId(UUID.randomUUID().toString().replace("-", ""));
            adminUserRepository.save(user);
            log.info("init admin user");
        }
    }

    public void addAdminUser(AdminUser user, HttpSession session) throws IntelligentException {
        AdminUser sessionUser = (AdminUser) session.getAttribute(Constant.USER_SESSION_KEY);
        if (sessionUser == null) {
            throw new IntelligentException("请先登录");
        }
        if (!sessionUser.getRoleType().equals(Constant.RoleType.LEVEL1)
                && !sessionUser.getRoleType().equals(Constant.RoleType.LEVEL2)) {
            throw new IntelligentException("无权限添加用户");
        }
        if (sessionUser.getRoleType().equals(Constant.RoleType.LEVEL2) &&
                user.getRoleType().equals(Constant.RoleType.LEVEL1)) {
            throw new IntelligentException("您没有权限添加超级用户");
        }
        checkAdminExist(user);
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setCreateTime(System.currentTimeMillis());
        user.setLastUpdateTime(user.getCreateTime());
        adminUserRepository.save(user);
    }

    public AdminListResponse getAdminUser(AdminListRequest request, HttpSession session) throws IntelligentException {
        AdminUser sessionUser = (AdminUser) session.getAttribute(Constant.USER_SESSION_KEY);
        if (sessionUser == null) {
            throw new IntelligentException("请先登录");
        }
        if (sessionUser.getRoleType() > Constant.RoleType.LEVEL2) {
            throw new IntelligentException("无权限操作");
        }
        Criteria criteria = new Criteria();
        Query query = new Query();
        if (sessionUser.getRoleType().equals(Constant.RoleType.LEVEL2)) {
            criteria.and("roleType").gte(Constant.RoleType.LEVEL2);
        }
        if (!StringUtils.isEmpty(request.getProperty())) {
            criteria.and(request.getProperty()).regex(request.getMatch());
        }
        query.addCriteria(criteria);
        long total = template.count(query, AdminUser.class);
        if (request.getPageNum() != null && request.getPageSize() != null) {
            Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize());
            query.with(pageable);
        }
        AdminListResponse response = new AdminListResponse();
        response.setPageNum(request.getPageNum());
        response.setPageSize(request.getPageSize());
        response.setTotal(total);
        response.setAdminUsers(template.find(query, AdminUser.class));
        return response;
    }

    public void delAdminUser(Set<String> ids,HttpSession session) throws IntelligentException {
        AdminUser sessionUser = (AdminUser) session.getAttribute(Constant.USER_SESSION_KEY);
        if (sessionUser == null) {
            throw new IntelligentException("请先登录");
        }
        if (sessionUser.getRoleType() > Constant.RoleType.LEVEL2) {
            throw new IntelligentException("无权限操作");
        }
        if (ids.isEmpty()) {
            return;
        }
        List<AdminUser> l1 = adminUserRepository.findAllByRoleType(Constant.RoleType.LEVEL1);
        Set<String> l1Ids = l1.stream().map(l -> l.getId()).collect(Collectors.toSet());
        if (sessionUser.getRoleType().equals(Constant.RoleType.LEVEL2) && !l1Ids.isEmpty()){
            throw new IntelligentException("您无法删除超级管理员");
        }
        l1Ids.removeAll(ids);
        if (l1Ids.isEmpty()) {
            throw new IntelligentException("请至少保留一个超级管理员账户!");
        }
        List<AdminUser> adminUsers = adminUserRepository.findByIdIn(ids);
        if (adminUsers.size() == 0) {
            log.info("查询失败");
            return;
        }
        log.info("管理账号:{}即将被删除", JSONObject.toJSONString(adminUsers));
        adminUserRepository.deleteAll(adminUsers);
    }

    public void checkAdminExist(AdminUser user) throws IntelligentException {
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new IntelligentException("请设置账号密码");
        }
        if (!StringUtils.isEmpty(user.getAdminName())) {
            AdminUser uc = adminUserRepository.findFirstByAdminName(user.getAdminName());
            if (uc != null) {
                throw new IntelligentException("用户名重复");
            }
        }
        if (!StringUtils.isEmpty(user.getCellPhone())) {
            AdminUser uc = adminUserRepository.findFirstByCellPhone(user.getCellPhone());
            if (uc != null) {
                throw new IntelligentException("手机号码重复");
            }
        }
        if (!StringUtils.isEmpty(user.getIdent())) {
            AdminUser uc = adminUserRepository.findFirstByIdent(user.getIdent());
            if (uc != null) {
                throw new IntelligentException("证件号已存在");
            }
        }
        List<AdminUser> adminUsers = adminUserRepository.findAllByRoleType(user.getRoleType());
        switch (user.getRoleType()) {
            case 1: {
                if (adminUsers.size() >= 4) {
                    throw new IntelligentException("该角色的账号容量达到上限");
                }
                break;
            }
            case 2:
            case 3:
            case 4: {
                if (adminUsers.size() >= 11) {
                    throw new IntelligentException("该角色的账号容量达到上限");
                }
                break;
            }
            default:
                throw new IntelligentException("不支持的角色");
        }
    }

    public AdminUser getAdminUser(String adminName) {
        return adminUserRepository.findFirstByAdminName(adminName);
    }

}
