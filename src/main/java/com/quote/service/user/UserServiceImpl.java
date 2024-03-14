package com.quote.service.user;

import com.quote.entity.user.UserEntity;
import com.quote.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity create(UserEntity userEntity, String userUuid) {
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setCreateUser(userUuid);
        userEntity.setCreateTime(Instant.now());
        userEntity.setIsDeleted(false);
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity login(UserEntity userEntity) {
        return userRepository.findByIsDeletedFalseAndAccountAndPassword(userEntity.getAccount(), userEntity.getPassword());
    }

    @Override
    public UserEntity findByUuid(String userUuid) {
        return userRepository.findByIsDeletedFalseAndUuid(userUuid);
    }

    @Override
    public UserEntity findByAccount(String account) {
        return userRepository.findByIsDeletedFalseAndAccount(account);
    }

}
