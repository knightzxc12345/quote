package com.design.entity.quote_detail;

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
    @NotBlank
    private String quoteUuid;

    // 廠商uuid
    @Column(
            name = "vote_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String voteUuid;

    // 廠商名稱
    @Column(
            name = "vote_name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String voteName;

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

    // 產品編號
    @Column(
            name = "product_no",
            nullable = true,
            updatable = true,
            unique = false,
            length = 10
    )
    private String productNo;

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

    // 產品客製單價
    @Column(
            name = "product_custom_unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productCustomUnitPrice;

    // 產品成本
    @Column(
            name = "product_cost_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productCostPrice;

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

    // 產品成本總計
    @Column(
            name = "product_cost_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productCostAmount;

    // 產品客製總計
    @Column(
            name = "product_custom_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal productCustomAmount;

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
