package com.design.entity.quote;

import com.design.entity.base.BaseEntity;
import com.design.entity.enums.QuoteStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@ToString(callSuper = true)
@Data
@Table(name = "quote", indexes = {
        @Index(name = "quote_find_all", columnList = "is_deleted, user_uuid, customer_uuid"),
})
@Entity
public class QuoteEntity extends BaseEntity {

    // 業務uuid
    @Column(
            name = "user_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String userUuid;

    // 業務名稱
    @Column(
            name = "user_name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String userName;

    // 業務uuid
    @Column(
            name = "customer_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String customerUuid;

    // 客戶名稱
    @Column(
            name = "customer_name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String customerName;

    // 客戶地址
    @Column(
            name = "customer_address",
            nullable = true,
            updatable = true,
            unique = false,
            length = 256
    )
    private String customerAddress;

    // 統一編號
    @Column(
            name = "customer_vat_number",
            nullable = true,
            updatable = true,
            unique = false,
            length = 20
    )
    private String customerVatNumber;

    // 承辦人員
    @Column(
            name = "under_taker_name",
            nullable = true,
            updatable = true,
            unique = false,
            length = 64
    )
    private String underTakerName;

    // 承辦人員電話
    @Column(
            name = "under_taker_mobile",
            nullable = true,
            updatable = true,
            unique = false,
            length = 32
    )
    private String underTakerMobile;

    // 合計
    @Column(
            name = "amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal amount;

    // 稅金
    @Column(
            name = "tax",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal tax;

    // 總計
    @Column(
            name = "total_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal totalAmount;

    // 客製合計
    @Column(
            name = "custom_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal customAmount;

    // 稅金
    @Column(
            name = "custom_tax",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal customTax;

    // 客製總計
    @Column(
            name = "custom_total_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal customerTotalAmount;

    // 成本合計
    @Column(
            name = "cost_amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal costAmount;

    // 報價單狀態
    @Column(
            name = "status",
            nullable = false,
            insertable = true,
            updatable = true,
            unique = false
    )
    @Convert(converter = QuoteStatus.Converter.class)
    @NotNull
    private QuoteStatus quoteStatus;

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
