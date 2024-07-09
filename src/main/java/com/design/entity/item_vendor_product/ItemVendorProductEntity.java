package com.design.entity.item_vendor_product;

import com.design.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

// 項目廠商產品
@ToString(callSuper = true)
@Data
@Table(name = "item_vendor_product", indexes = {
        @Index(name = "item_vendor_product_find_all", columnList = "is_deleted"),
})
@Entity
public class ItemVendorProductEntity extends BaseEntity {

    // 項目uuid
    @Column(
            name = "item_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID itemUuid;

    // 廠商產品uuid
    @Column(
            name = "vendor_product_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID vendorProductUuid;

    // 數量
    @Column(
            name = "qty",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private Integer qty;

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
