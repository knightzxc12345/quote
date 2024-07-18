package com.design.entity.vendor_quote_detail;

import com.design.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// 廠商報價單明細
@ToString(callSuper = true)
@Data
@Table(name = "vendor_quote_detail", indexes = {
        @Index(name = "vendor_quote_detail_find_vendor_quote_uuid", columnList = "is_deleted, vendor_quote_uuid"),
})
@Entity
public class VendorQuoteDetailEntity extends BaseEntity {

    // 廠商報價單uuid
    @Column(
            name = "vendor_quote_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID vendorQuoteUuid;

    // 品項uuid
    @Column(
            name = "item_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID itemUuid;

    // 品項編號
    @Column(
            name = "item_no",
            nullable = true,
            updatable = true,
            unique = false,
            length = 10
    )
    private String itemNo;

    // 品項名稱
    @Column(
            name = "item_name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String itemName;

    // 品項規格
    @Column(
            name = "item_spec",
            nullable = false,
            updatable = true,
            unique = false,
            length = 256
    )
    @NotBlank
    private String itemSpec;

    // 品項單位
    @Column(
            name = "item_unit",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String itemUnit;

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

    // 產商產品單價
    @Column(
            name = "vendor_product_unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal vendorProductUnitPrice;

    // 廠商產品數量
    @Column(
            name = "quantity",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Min(0)
    @NotNull
    private Integer quantity;

    // 廠商產品總計
    @Column(
            name = "vendor_product_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal vendorProductAmount;

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
