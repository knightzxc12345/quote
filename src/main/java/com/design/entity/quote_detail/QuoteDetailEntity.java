package com.design.entity.quote_detail;

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

// 保價單明細
@ToString(callSuper = true)
@Data
@Table(name = "quote_detail", indexes = {
        @Index(name = "quote_detail_find", columnList = "is_deleted, quote_uuid"),
})
@Entity
public class QuoteDetailEntity extends BaseEntity {

    // 報價單uuid
    @Column(
            name = "quote_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotNull
    private UUID quoteUuid;

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
            nullable = false,
            updatable = true,
            unique = false,
            length = 32
    )
    @NotBlank
    private String itemUnit;

    // 品項廠商產品單價
    @Column(
            name = "item_vendor_product_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductPrice;

    // 品項廠商產品客製單價
    @Column(
            name = "item_vendor_product_custom_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductCustomPrice;

    // 品項廠商產品成本單價
    @Column(
            name = "item_vendor_product_cost_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductCostPrice;

    // 數量
    @Column(
            name = "quantity",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private Integer quantity;

    // 品項廠商產品總計
    @Column(
            name = "item_vendor_product_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductAmount;

    // 品項廠商產品客製總計
    @Column(
            name = "item_vendor_product_custom_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductCustomAmount;

    // 品項廠商產品成本總計
    @Column(
            name = "item_vendor_product_cost_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal itemVendorProductCostAmount;

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
