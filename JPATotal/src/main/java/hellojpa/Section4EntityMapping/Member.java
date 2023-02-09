package hellojpa.Section4EntityMapping;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//@Entity
//@Table(name = "Section4")
public class Member {

    @Id //pk맵핑
    private Long id;

    @Column(name = "name", insertable = true, updatable = true) //객체는 username db는 name이라고 사용하고 싶다
    private String username;
    /*
    name = 필드와 매핑할 테이블의 컬럼이름
    insertable, updatable = 등록, 변경 가능 여부  column을 수정했을때 database에 수정을 할꺼야 말꺼야
    updatable = false로 하면 컬럼이 절대로 변경되지않는다 db에서 강제로 업데이트치면 가능함 JPA로는 절대불가능
    nullable = false 라고하면 notnull 제약조건이 걸린다
    unique = true 라고하면 유니크 제약조건이 걸린다 그러나 못알아보는 이름이 걸려서 잘 사용하지 않는다 그래서 Table에서 Unique (uniqueConstraints=) 제약조건을 건다
    length = 10 이라고사용한다면 varchar(10)이런식으로 걸린다
    columDefiniton = "varchar(100) default 'EMPTY'" 이라고 직접 정의해 줄 수도 있다
    precision, scale = 숫자가 엄청큰경우 BigDecimal ,BigInteger 타입에서 사용한다
    precision은 소수점을 포함한 전체 자릿수를, scale은 소수의 자릿수다
    참고로 double, float 타입에는 적용되지 않는다, 아주 큰 숫자나 정밀한 소수를 다루어야 할 때만 사용한다.
     */
//    @Column()
//    private BigDecimal age;

    private Integer age; //Integer와 가장 적절한 숫자 타입이 설정이 된다


//    @Enumerated
    //객체에서 EnumType을 사용하고싶다 순서대로 저장이되기때문에 객체의 내용이 추가가되면 순서가 바뀌어버려
    // 해결할수 없는 오류가 생긴다 그래서 ORDINAL 타입을 사용하는 것은 좋은 방법이 아니다. RoleType이 숫자로 적용이되며(앞에서부터 0,1,2)이런식으로 적용이된다
    //원래 0번이었던 USER가 앞에 GUEST가 생기면서 1번이되고 GUEST가 0번이되지만 DB는 적용이 되지 않기떄문에 기존의 데이터들과 섞이게된다
    //그래서 Enum타입에 default값인 ORDINAL을 사용하면 굉장히 위험하다
//    private RoleType roleType;


    @Enumerated(EnumType.STRING) //객체에서 EnumType을 사용하고싶다
    private RoleType roleType;
    //ROLETYPE이  enum에 저장한 문자형으로  DB에 적용이되며(GUEST, USER, ADMIN)의 이름으로 저장이된다


    /*
    EnumType의 기본 default ORDINAL
    EnumType.ORDINAL: enum 순서를 데이터베이스에 저장
    EnumType.String : enum 이름을 데이터베이스에 저장
     */



    @Temporal(TemporalType.TIMESTAMP) //날짜타입을 사용하고싶다 Temporal 타입이 세가지있다 Date(날짜),Time(시간),Timestamp(날짜시간)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    //최신버전인 요즘은 Temporal은 잘안쓰고 바로 형으로 사용한다 그러나 과거버전은 Temporal을 사용해야한다
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob //varchar를 넘는 큰컨텐츠를 넣고싶으면 Lob을 쓰면된다
    private String description;

    /*
    데이터베이스 BLOB, CLOB 타입과 매핑
    @Lob에는 지정할 수 있는 속성이 없다.
    매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB매핑
        CLOB: String, char[], java.sql.CLOB
        BLOB: byte[],java.sql.BLOB
     */

    @Transient //db에 생성되지않음 그냥 메모리에서만 사용하겠다는 뜻
    private int temp;
    /*
    @Transient
    필드 매핑 X
    데이터베이스에 저장X, 조회X
    주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용
     */


    /*
    Column 컬럼매핑
    Temporal 날짜 타입 매핑
    Enumerated enum 타입 매핑
    Lob BLOB, CLOB매핑
    Transient 특정 필드를 컬럼에 매핑하지 않음 (매핑 무시)
     */


    /*
    id bigint not null,
    age integer,
    createdDate timestamp,
    description clob,
    lastModifiedDate timestamp,
    roleType varchar(255),
    name varchar(255),
    primary key (id)
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Member(){



    }
}
