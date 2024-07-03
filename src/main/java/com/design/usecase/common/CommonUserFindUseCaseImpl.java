package com.design.usecase.common;

import com.design.controller.common.response.CommonUserFindAllResponse;
import com.design.entity.user.UserEntity;
import com.design.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonUserFindUseCaseImpl implements CommonUserFindUseCase {

    private final UserService userService;

    @Override
    public List<CommonUserFindAllResponse> findAllByRoleUuid(UUID roleUuid) {
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
