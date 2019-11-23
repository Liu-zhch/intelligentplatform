package com.lzc.intelligentplatform.entity;

import com.lzc.intelligentplatform.util.Constant;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Document(collection = "admin_user")
public class AdminUser {
    private String id;
    @NotNull(message = "管理员名字不能为空")
    private String adminName;
    /**
     * 1,一级管理员 2，二级管理员 3，三级管理员 4,四级管理员
     */
    @Range(min = 1, max = 4, message = "角色类型不合法")
    private Integer roleType;
    /**
     * 身份证号码
     */
    @Pattern(regexp = Constant.Reg.ident, message = "身份证号码有错，请检查")
    private String ident;
    /**
     * 1,男 2，女
     */
    @Range(min = 1, max = 2, message = "性别参数不合法")
    private Integer sex;

    /**
     * 手机号码
     */
    @Pattern(regexp = Constant.Reg.cellPhone, message = "手机号码有错，请检查")
    private String cellPhone;
    /**
     * 登录密码
     */
    @NotNull(message = "请设置密码")
    @Size(min = 6, max = 12, message = "请设置6~12位密码")
    private String password;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 最近更新时间
     */
    private Long lastUpdateTime;
}
