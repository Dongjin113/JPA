package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		//강제 지연 로딩 설정
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}
/*
	엔티티 설계시 주의점
	-엔티티에는 가급적 Setter를 사용하지 말자
		=Setter가 모두 열려있다. 변경포인트가 너무 많아서, 유지보수가 어렵다.
	-모든 연관관계는 지연로딩으로 설정!
	-컬렉션은 필드에서 초기화 하자.

*/
}
