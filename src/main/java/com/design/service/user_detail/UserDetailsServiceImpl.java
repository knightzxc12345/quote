package com.design.service.user_detail;

import com.design.entity.user.UserEntity;
import com.design.modle.User;
import com.design.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserById(String userName) {
        UUID userUuid = UUID.fromString(userName);
        UserEntity userEntity = userService.findByUuid(userUuid);
        return new User(userEntity);
    }

}
