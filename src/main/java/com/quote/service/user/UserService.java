package com.quote.service.user;

import com.quote.entity.user.UserEntity;

public interface UserService {

    UserEntity create(UserEntity userEntity, String userUuid);

    UserEntity login(UserEntity userEntity);

    UserEntity findByUuid(String userUuid);

    UserEntity findByAccount(String account);

}
