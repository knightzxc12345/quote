package com.design.service.user_detail;

import com.design.entity.user.UserEntity;
import com.design.modle.User;
import com.design.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserById(String userName) {
        UserEntity userEntity = userService.findByUuid(userName);
        return new User(userEntity);
    }

}
