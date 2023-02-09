package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.MappingSuperClass;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@MappedSuperclass //매핑정보만 받는 부모클래스 슈퍼클래스라고 보면된다
public abstract class BaseEntity {

    /* 전체적으로 공통적으로 여기있는 속성을 같이 쓰고싶을때 사용하는 것*/

    @Column(name = "INSERT_MEMBER")
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedMBy;

    @Column(name = "UPDATE_MEMBER")
    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedMBy() {
        return lastModifiedMBy;
    }

    public void setLastModifiedMBy(String lastModifiedMBy) {
        this.lastModifiedMBy = lastModifiedMBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
