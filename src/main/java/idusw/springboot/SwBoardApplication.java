package idusw.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EnableJpaAuditing
public class SwBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwBoardApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {    //put, delete 처리
        return new HiddenHttpMethodFilter();
    }
    //메소드를 호출하여 Bean 객체를 생성 ,HTTP 메소드 중 GET POST를 제외한 다른 메소드(hidden) 필터링
    //모든 브라우저가 hidden 메소드를 잘 지원하지 않는다, 언어에 따라 다름
    //필터링해서 가능하게 해줌(Thymeleaf 에서는 100%가능)
}
