package com.quote.entity.vendor;

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
@Table(name = "vendor", indexes = {
        @Index(name = "vendor_find", columnList = "is_deleted, name"),
})
@Entity
public class VendorEntity extends BaseEntity {

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

    // 地址
    @Column(
            name = "address",
            nullable = true,
            updatable = true,
            unique = false,
            length = 256
    )
    private String address;

    // 手機
    @Column(
            name = "mobile",
            nullable = true,
            updatable = true,
            unique = false,
            length = 20
    )
    private String mobile;

    // 電話
    @Column(
            name = "tel",
            nullable = true,
            updatable = true,
            unique = false,
            length = 20
    )
    private String tel;

    // 傳真
    @Column(
            name = "fax",
            nullable = true,
            updatable = true,
            unique = false,
            length = 20
    )
    private String fax;

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
