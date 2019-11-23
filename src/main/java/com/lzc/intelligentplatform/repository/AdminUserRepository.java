package com.lzc.intelligentplatform.repository;

import com.lzc.intelligentplatform.entity.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminUserRepository extends MongoRepository<AdminUser,String> {
    AdminUser findFirstByRoleType(Integer type);
    AdminUser findFirstByAdminName(String name);
    AdminUser findFirstByCellPhone(String phone);
    AdminUser findFirstByIdent(String ident);
    List<AdminUser> findAllByRoleType(Integer type);
    List<AdminUser> findByIdIn(Set<String> ids);
}
