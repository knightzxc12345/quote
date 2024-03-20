package com.design.service.user;

import com.design.entity.user.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity create(UserEntity userEntity, String userUuid);

    UserEntity login(UserEntity userEntity);

    UserEntity findByUuid(String userUuid);

    UserEntity findByAccount(String account);

    List<UserEntity> findByRoleUuid(String roleUuid);

}
