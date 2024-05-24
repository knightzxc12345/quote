package com.design.entity.vendor_quote;

import com.design.entity.base.BaseEntity;
import com.design.entity.enums.VendorQuoteStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

// 廠商報價單
@ToString(callSuper = true)
@Data
@Table(name = "vendor_quote", indexes = {
        @Index(name = "vendor_quote_find_all", columnList = "is_deleted, vendor_uuid, customer_uuid, create_time"),
})
@Entity
public class VendorQuoteEntity extends BaseEntity {

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

    // 客戶uuid
    @Column(
            name = "customer_uuid",
            nullable = false,
            updatable = true,
            unique = false,
            length = 36
    )
    @NotBlank
    private String customerUuid;

    // 合計
    @Column(
            name = "amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal amount;

    // 廠商報價單狀態
    @Column(
            name = "status",
            nullable = false,
            insertable = true,
            updatable = true,
            unique = false
    )
    @Convert(converter = VendorQuoteStatus.Converter.class)
    @NotNull
    private VendorQuoteStatus vendorQuoteStatus;

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
