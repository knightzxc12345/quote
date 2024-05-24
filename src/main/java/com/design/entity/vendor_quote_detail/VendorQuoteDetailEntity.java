package com.design.entity.vendor_quote_detail;

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
    @NotBlank
    private String vendorQuoteUuid;

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

    // 產品uuid
    @Column(
            name = "product_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String productUuid;

    // 產品規格
    @Column(
            name = "product_specification",
            nullable = false,
            updatable = true,
            unique = false,
            length = 256
    )
    @NotBlank
    private String productSpecification;

    // 產品單位
    @Column(
            name = "product_unit",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String productUnit;

    // 產品單價
    @Column(
            name = "product_unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productUnitPrice;

    // 產品數量
    @Column(
            name = "product_quantity",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private Integer productQuantity;

    // 產品總計
    @Column(
            name = "product_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productAmount;

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
