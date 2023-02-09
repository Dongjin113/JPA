package hellojpa.Section9ValueType.ValueTypeCompare;

public class CompareMain {
    /* 값타입의 비교
    -값 타입: 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야 함
    int a = 10;
    int b = 10;
    Address a = new Address("서울시");
    Address b = new Address("서울시");
    */

    public static void main(String[] args) {
        int a = 10;
        int b = 10;
        System.out.println("a == b: "+(a==b)); //true

        Address address1 = new Address("city","street","10000");
        Address address2 = new Address("city","street","10000");

        System.out.println("address1 == address2: "+(address1==address2));
        System.out.println("address1 equals address2: "+(address1.equals(address2)));
        // equals를 재정의하고 사용하지 않으면 false 재정의 해주면 true가나온다 equals의 default값이 == 이기떄문이다
        //값 타입들의 비교는 항상 equals를 사용해야한다


        /* 값 타입의 비교
            - 동일성(identity)비교 : 인스턴스의 참조 값을 비교, == 사용
            - 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equals() 사용
            - 값 타입은 a.equals(b)를 사용해서 동등성 비교를 해야 함
            - 값 타입의 equals() 메소드를 적절학 재정의 (주로 모든필드 사용)
            */


    }
}
