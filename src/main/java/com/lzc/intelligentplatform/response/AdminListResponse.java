package com.lzc.intelligentplatform.response;

import com.lzc.intelligentplatform.entity.AdminUser;
import com.lzc.intelligentplatform.request.RequestPage;
import lombok.Data;

import java.util.List;

@Data
public class AdminListResponse extends RequestPage {
    private List<AdminUser> adminUsers;
}
