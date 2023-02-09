package hellojpa.Section4EntityMapping;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/*
@Entity
//hibernate sequence가 아닌  내가 설정하여 SEQUENCE 매핑하기
@SequenceGenerator(
        name= "PKMEMBER_SEQ_GENERATOR",
        sequenceName = "PKMEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 50)
//        initialValue : 초기 값, 마지막으로 생성된 값이 기준이다.
//        allocationSize : 시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용됨)
*/

/* 테이블 전략\
    -키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
    -장점 : 모든 데이터베이스에 적용 가능
    -단점 : 성능
    */

/*@TableGenerator(
        name = "PKMEMBER_SEQ_GENERATOR", //이름
        table = "PKMY_SEQUENCES", //테이블명
        pkColumnValue = "PKMEMBER_SEQ", allocationSize = 1 //pk컬럼명
)*/

public class PKMember {
    /*
    기본 키 매핑 방법
    직접 할당: @Id만 사용

    자동 생성(@GeneratedValue)
        - IDENTITY: 데이터베이스에 위임, MYSQL
        - SEQUENCE: 데이터 베이스 시퀀스 오브젝트 사용, ORACLE
            - @SequenceGenerator 필요
        - TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용
            - @TableGenerator 필요
        - AUTO: 방언에 따라 자동 지정, 기본값
     */

/*    ==============@Id사용하기====================
    @Id //내가직접 ID를 세팅해서 할당한다 할때는 @Id만 사용하면된다
    private String id;
*/
/*
//               ===========GenerationType.IDENTITY 사용하기=============
//    @GeneratedValue(strategy = GenerationType.AUTO) //기본값은 auto = DB방언에 맞춰서 자동으로 생성이 된다
//    IDENTITY = 기본키 생성을 데이터 베이스에 위임 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용(예: MySQL의 AUTO_INCREMENT)
//    String의 형으로 실행하면 실행이 안된다 IDENTITY가 실행이 안돼 persistence의 jdbc url에 MODE=LEGACY를 추가해줬더니 실행이 잘 된다
//    IDENTITY 란 ? 난 모르겠고 DB야 니가 알아서해줘

        IDENTITY전략 -특징
        -기본 키 생성을 데이터베이스에 위임
        -주로 MYSQL, PostgreSQL, SQL Server, DB2에서 사용(예: MySQL의 AUTO_INCREMENT)
        -JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행
        -AUTO_INCREMENT는 데이터베이스에 INSERT SQL을 실행한 이후에 ID값을 알 수 있음
        -IDENTITY 전략은 em.persist()시점에 즉시 INSERT SQL 실행하고 DB에서 식별자를 조회
            (데이터베이스에 값이 들어갈때까지 Id값을 알 수 없기 떄문에)
 */
/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
*/



    /*
//    자동 시퀀스
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
     */
//    내가 시퀀스 생성하여 사용하기
//    ==================@SequenceGenerator 사용하기=====================
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKMEMBER_SEQ_GENERATOR")
    private Long id;

    //===================TableGenerator사용하기============
/*    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PKMEMBER_SEQ_GENERATOR")
    private Long id;*/


    @Column(name = "name", nullable = false)
    private String username;

    public PKMember(){
    }

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
}

/*
권장하는 식별자 전략
기본키 제약조건: null 아님 , 유일, 변하면 안된다.
미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자.
예를 들어 주민등록번호도 기본 키로 적절하지 않다.
권장: Long형 + 대체키 + 키 생성전략 사용
*/

