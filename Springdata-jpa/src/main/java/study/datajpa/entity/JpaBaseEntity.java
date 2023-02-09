package study.datajpa.entity;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persist하기전 이벤트가 발생
    public void perPersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }


}
