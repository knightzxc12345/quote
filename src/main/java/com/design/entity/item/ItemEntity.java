package com.design.entity.item;

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

// 項目
@ToString(callSuper = true)
@Data
@Table(name = "item", indexes = {
        @Index(name = "item_find_all", columnList = "is_deleted, name")
})
@Entity
public class ItemEntity extends BaseEntity {

    // 編號
    @Column(
            name = "no",
            nullable = true,
            updatable = true,
            unique = false,
            length = 10
    )
    private String no;

    // 品名
    @Column(
            name = "name",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String name;

    // 規格
    @Column(
            name = "spec",
            nullable = false,
            updatable = true,
            unique = false,
            length = 256
    )
    @NotBlank
    private String spec;

    // 單位
    @Column(
            name = "unit",
            nullable = false,
            updatable = true,
            unique = false,
            length = 64
    )
    @NotBlank
    private String unit;

    // 總計
    @Column(
            name = "amount",
            nullable = false,
            updatable = true,
            unique = false
    )
    @Digits(integer = 10, fraction = 0)
    @Min(0)
    @NotNull
    private BigDecimal amount;

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
