package com.design.repository.user;

import com.design.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIsDeletedFalseAndAccountAndPassword(String account, String password);

    UserEntity findByIsDeletedFalseAndUuid(UUID userUuid);

    UserEntity findByIsDeletedFalseAndAccount(String account);

    List<UserEntity> findByIsDeletedFalseAndRoleUuidOrderByNameAsc(UUID roleUuid);

}
