package com.quote.service.user_detail;

import com.quote.entity.user.UserEntity;
import com.quote.modle.User;
import com.quote.service.user.UserService;
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
