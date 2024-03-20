package com.design.service.user;

import com.design.entity.user.UserEntity;
import com.design.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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

    @Override
    public List<UserEntity> findByRoleUuid(String roleUuid) {
        return userRepository.findByIsDeletedFalseAndRoleUuidOrderByNameAsc(roleUuid);
    }

}
