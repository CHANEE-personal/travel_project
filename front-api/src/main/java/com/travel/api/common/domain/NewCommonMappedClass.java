package com.travel.api.common.domain;

import lombok.*;
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
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class NewCommonMappedClass {

    @CreatedBy
    @Column(name = "creator", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updater")
    private String modifiedBy;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;
}
