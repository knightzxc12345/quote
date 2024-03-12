package com.quote.service.init;

import com.quote.base.Common;
import com.quote.entity.user.UserEntity;
import com.quote.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InitUserServiceImpl implements InitUserService {

    @Autowired
    private final UserService userService;

    @Override
    public void init() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("鹿");
        userEntity.setAccount("tim");
        userEntity.setPassword("Aa000000");
        if(isExists(userEntity)){
            return;
        }
        userService.create(userEntity, Common.SYSTEM);
    }

    private boolean isExists(UserEntity userEntity){
        UserEntity isExists = userService.findByAccount(userEntity.getAccount());
        return null != isExists;
    }

}
