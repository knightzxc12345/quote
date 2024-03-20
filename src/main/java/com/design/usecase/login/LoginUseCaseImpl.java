package com.design.usecase.login;

import com.design.controller.common.request.LoginRequest;
import com.design.controller.common.response.CommonLoginResponse;
import com.design.entity.user.UserEntity;
import com.design.service.user.UserService;
import com.design.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserService userService;

    @Override
    public CommonLoginResponse login(LoginRequest loginRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccount(loginRequest.account());
        userEntity.setPassword(loginRequest.password());
        userEntity = userService.login(userEntity);
        if(null == userEntity){
            return new CommonLoginResponse(
                    null,
                    null
            );
        }
        String token = JwtUtil.generateToken(userEntity.getUuid());
        return new CommonLoginResponse(
                userEntity.getName(),
                token
        );
    }

}
