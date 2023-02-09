package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity(name="Member")
//@Table(name = "MBR") //member가아닌 user테이블에 저장하고싶으면 이런식으로 사용해주면 된다
//<property name="hibernate.hbm2ddl.auto" value="create" /> 애플리케이션 로딩시점에 Entity가 매핑된 애들은 보고 기존에 있던 테이블은 다 지우고 테이블을 다 만들어낸다
//JPA를 사용하는애구나 관리해야겠다라고 인식을한다
public class Member {

    @Id //pk가 뭔지 알려준다
    private Long id;
    //@Column(name = "username") 컬럼명을 정할수 있다
    @Column(unique = true, length = 10, name = "USERNAME")
    private String name;

    private String gender;
    private int number;

    public Member() {
    }

    //JPA는 사용할려면 기본생성자가 한개 있어야한다
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
