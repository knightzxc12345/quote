package com.design.entity.vendor_product;

import com.design.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// 廠商產品
@ToString(callSuper = true)
@Data
@Table(name = "vendor_product", indexes = {
        @Index(name = "vendor_product_find_all", columnList = "is_deleted, name"),
})
@Entity
public class VendorProductEntity extends BaseEntity {

    // 廠商uuid
    @Column(
            name = "vendor_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID vendorUuid;

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

    // 單價
    @Column(
            name = "unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal unitPrice;

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
    private UUID deletedUser;

}
