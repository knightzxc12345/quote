package com.quote.entity.user;

import com.quote.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@ToString(callSuper = true)
@Data
@Table(name = "user", indexes = {
        @Index(name = "user_find_all", columnList = "is_deleted, account, password"),
})
@Entity
public class UserEntity extends BaseEntity {

    // 名稱
    @Column(
            name = "name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String name;

    // 帳號
    @Column(
            name = "account",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String account;

    // 密碼
    @Column(
            name = "password",
            nullable = false,
            updatable = true,
            unique = false,
            length = 128
    )
    @NotBlank
    private String password;

    // 手機
    @Column(
            name = "mobile",
            nullable = false,
            updatable = true,
            unique = false,
            length = 15
    )
    @NotBlank
    private String mobile;

    // 角色
    @Column(
            name = "role_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String roleUuid;

    // 是否刪除
    @Column(
            name = "is_deleted",
            nullable = false,
            insertable = true,
            updatable = true,
            unique = false
    )
    @NotNull
    private Boolean isDeleted;

    // 刪除時間
    @Column(
            name = "deleted_time",
            nullable = true,
            insertable = true,
            updatable = true,
            unique = false
    )
    private Instant deletedTime;

    // 刪除人員
    @Column(
            name = "deleted_user",
            nullable = true,
            insertable = true,
            updatable = true,
            unique = false,
            length = 36
    )
    private String deletedUser;

}
