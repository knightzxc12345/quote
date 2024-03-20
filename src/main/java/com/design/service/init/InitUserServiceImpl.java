package com.design.service.init;

import com.design.base.Common;
import com.design.entity.user.UserEntity;
import com.design.service.user.UserService;
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
        userEntity.setName("劉建廷");
        userEntity.setAccount("tim");
        userEntity.setPassword("Aa000000");
        userEntity.setMobile("0912686525");
        userEntity.setRoleUuid(Common.BUSINESS);
        if(isExists(userEntity)){
            return;
        }
        userService.create(userEntity, Common.SYSTEM);
        userEntity = new UserEntity();
        userEntity.setName("林淑樺");
        userEntity.setAccount("joyce");
        userEntity.setPassword("Aa000000");
        userEntity.setMobile("0978510507");
        userEntity.setRoleUuid(Common.BUSINESS);
        if(isExists(userEntity)){
            return;
        }
        userService.create(userEntity, Common.SYSTEM);
        userEntity = new UserEntity();
        userEntity.setName("李宜靜");
        userEntity.setAccount("boss");
        userEntity.setPassword("Aa000000");
        userEntity.setMobile("0925218238");
        userEntity.setRoleUuid(Common.MANAGER);
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
