package com.quote.usecase.user;

import com.quote.controller.common.response.UserFindResponse;
import com.quote.entity.user.UserEntity;
import com.quote.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFindUseCaseImpl implements UserFindUseCase {

    private final UserService userService;

    @Override
    public List<UserFindResponse> findAllByRoleUuid(String roleUuid) {
        List<UserEntity> userEntities = userService.findByRoleUuid(roleUuid);
        return format(userEntities);
    }

    private List<UserFindResponse> format(List<UserEntity> userEntities){
        List<UserFindResponse> responses = new ArrayList<>();
        if(null == userEntities || userEntities.isEmpty()){
            return responses;
        }
        for(UserEntity userEntity : userEntities){
            responses.add(new UserFindResponse(
                    userEntity.getUuid(),
                    userEntity.getName(),
                    userEntity.getMobile()
            ));
        }
        return responses;
    }

}
