package idusw.springboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class BaseEntity { //감사 목적,

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate; //생성일자


    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate; //수정일자
}
