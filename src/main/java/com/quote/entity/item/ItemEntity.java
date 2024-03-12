package com.quote.entity.item;

import com.quote.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString(callSuper = true)
@Data
@Table(name = "item", indexes = {
        @Index(name = "item_find", columnList = "is_deleted, name, desc"),
})
@Entity
public class ItemEntity extends BaseEntity {

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
            name = "desc",
            nullable = false,
            updatable = true,
            unique = false,
            length = 256
    )
    @NotBlank
    private String desc;

    // 單價
    @Column(
            name = "unit_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal unitPrice;

    // 原價
    @Column(
            name = "origin_price",
            nullable = false,
            updatable = true,
            unique = false
    )
    @NotNull
    private BigDecimal originPrice;

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
    private Date deletedTime;

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
