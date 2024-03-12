package com.quote.entity.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "pk",
            nullable = false,
            updatable = false,
            unique = true
    )
    private Long pk;

    // 唯一值
    @Column(
            name = "uuid",
            nullable = false,
            updatable = false,
            unique = true,
            length = 36
    )
    @NotNull
    private String uuid;

    // 創建時間
    @Column(
            name = "create_time",
            nullable = false,
            updatable = false,
            unique = false
    )
    @NotNull
    private Date createTime;

    // 創建人員
    @Column(
            name = "create_user",
            nullable = false,
            insertable = true,
            updatable = false,
            unique = false,
            length = 36
    )
    @NotNull
    private String createUser;

    // 編輯時間
    @LastModifiedDate
    @Column(
            name = "modified_time",
            nullable = true,
            updatable = true,
            unique = false
    )
    private Date modifiedTime;

    // 編輯人員
    @Column(
            name = "modified_user",
            nullable = true,
            insertable = true,
            updatable = true,
            unique = false,
            length = 36
    )
    private String modifiedUser;

}
