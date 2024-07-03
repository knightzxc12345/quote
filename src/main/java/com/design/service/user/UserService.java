package com.design.service.user;

import com.design.entity.user.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity create(UserEntity userEntity, UUID userUuid);

    UserEntity login(UserEntity userEntity);

    UserEntity findByUuid(UUID userUuid);

    UserEntity findByAccount(String account);

    List<UserEntity> findByRoleUuid(UUID roleUuid);

}
