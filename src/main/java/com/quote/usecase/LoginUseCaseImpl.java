package com.quote.usecase;

import com.quote.controller.common.request.LoginRequest;
import com.quote.controller.common.response.LoginResponse;
import com.quote.entity.user.UserEntity;
import com.quote.service.user.UserService;
import com.quote.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserService userService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccount(loginRequest.account());
        userEntity.setPassword(loginRequest.password());
        userEntity = userService.login(userEntity);
        if(null == userEntity){
            return new LoginResponse(
                    null,
                    null
            );
        }
        String token = JwtUtil.generateToken(userEntity.getUuid());
        return new LoginResponse(
                userEntity.getName(),
                token
        );
    }

}
