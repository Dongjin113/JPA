package hellojpa.Section10JPQLBasicGrammar.JPQLBasicFunction.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

//h2데이터베이스를 사용하기때문에 H2Dialect를 상속받아 사용한다
//사용방법은 H2Dialect 코드를  참조해서 사용한다
public class MyH2Dialect  extends H2Dialect {

    public MyH2Dialect (){
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }

}
