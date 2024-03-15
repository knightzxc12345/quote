package com.quote.entity.customer;

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
@Table(name = "customer", indexes = {
        @Index(name = "customer_find", columnList = "is_deleted, name"),
})
@Entity
public class CustomerEntity extends BaseEntity {

    // 客戶名稱
    @Column(
            name = "name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String name;

    // 客戶地址
    @Column(
            name = "address",
            nullable = true,
            updatable = true,
            unique = false,
            length = 256
    )
    private String address;

    // 協理名稱
    @Column(
            name = "deputy_manager_name",
            nullable = true,
            updatable = true,
            unique = false,
            length = 64
    )
    private String deputyManagerName;

    // 協理電話
    @Column(
            name = "deputy_manager_mobile",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String deputyManagerMobile;

    // 協理信箱
    @Column(
            name = "deputy_manager_email",
            nullable = true,
            updatable = true,
            unique = false,
            length = 128
    )
    private String deputyManagerEmail;

    // 主管名稱
    @Column(
            name = "manager_name",
            nullable = true,
            updatable = true,
            unique = false,
            length = 64
    )
    private String managerName;

    // 主管電話
    @Column(
            name = "manager_mobile",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String managerMobile;

    // 主管信箱
    @Column(
            name = "manager_email",
            nullable = true,
            updatable = true,
            unique = false,
            length = 128
    )
    private String managerEmail;

    // 總務名稱
    @Column(
            name = "general_affairs_manager_name",
            nullable = true,
            updatable = true,
            unique = false,
            length = 64
    )
    private String generalAffairsManagerName;

    // 總務電話
    @Column(
            name = "general_affairs_manager_mobile",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String generalAffairsManagerMobile;

    // 總務信箱
    @Column(
            name = "general_affairs_manager_email",
            nullable = true,
            updatable = true,
            unique = false,
            length = 128
    )
    private String generalAffairsManagerEmail;

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
