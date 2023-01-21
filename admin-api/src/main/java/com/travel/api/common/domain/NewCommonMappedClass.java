package com.travel.api.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class NewCommonMappedClass {

    @CreatedBy
    @Column(name = "creator", updatable = false)
    @ApiModelProperty(required = true, value = "등록자")
    private Long creator;

    @LastModifiedBy
    @Column(name = "updater")
    @ApiModelProperty(required = true, value = "수정자")
    private Long updater;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(required = true, value = "등록 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @ApiModelProperty(required = true, value = "수정 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
