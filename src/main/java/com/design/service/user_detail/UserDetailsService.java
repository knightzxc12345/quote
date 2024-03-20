package com.design.service.user_detail;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {

    UserDetails loadUserById(String userName);

}
