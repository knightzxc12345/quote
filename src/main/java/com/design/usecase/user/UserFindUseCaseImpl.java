package com.design.usecase.user;

import com.design.controller.common.response.CommonUserFindAllResponse;
import com.design.entity.user.UserEntity;
import com.design.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFindUseCaseImpl implements UserFindUseCase {

    private final UserService userService;

    @Override
    public List<CommonUserFindAllResponse> findAllByRoleUuid(String roleUuid) {
        List<UserEntity> userEntities = userService.findByRoleUuid(roleUuid);
        return format(userEntities);
    }

    private List<CommonUserFindAllResponse> format(List<UserEntity> userEntities){
        List<CommonUserFindAllResponse> responses = new ArrayList<>();
        if(null == userEntities || userEntities.isEmpty()){
            return responses;
        }
        for(UserEntity userEntity : userEntities){
            responses.add(new CommonUserFindAllResponse(
                    userEntity.getUuid(),
                    userEntity.getName(),
                    userEntity.getMobile()
            ));
        }
        return responses;
    }

}
