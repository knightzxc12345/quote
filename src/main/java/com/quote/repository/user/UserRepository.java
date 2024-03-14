package com.quote.repository.user;

import com.quote.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIsDeletedFalseAndAccountAndPassword(String account, String password);

    UserEntity findByIsDeletedFalseAndUuid(String uuid);

    UserEntity findByIsDeletedFalseAndAccount(String account);

}
