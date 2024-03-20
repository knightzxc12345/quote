package com.design.entity.product;

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

@ToString(callSuper = true)
@Data
@Table(name = "product", indexes = {
        @Index(name = "product_find_all", columnList = "is_deleted, vendor_uuid, name, specification")
})
@Entity
public class ProductEntity extends BaseEntity {

    // 廠商uuid
    @Column(
            name = "vendor_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String vendorUuid;

    // 品項uuid
    @Column(
            name = "item_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String itemUuid;

    // 編號
    @Column(
            name = "no",
            nullable = true,
            updatable = true,
            unique = false,
            length = 10
    )
    private String no;

    // 規格
    @Column(
            name = "specification",
            nullable = false,
            updatable = true,
            unique = false,
            length = 256
    )
    @NotBlank
    private String specification;

    // 單位
    @Column(
            name = "unit",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String unit;

    // 單價
    @Column(
            name = "unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal unitPrice;

    // 成本
    @Column(
            name = "cost_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal costPrice;

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