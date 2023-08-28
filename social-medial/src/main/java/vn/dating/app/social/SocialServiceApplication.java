package vn.dating.app.social;


import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableEurekaClient

public class SocialServiceApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SocialServiceApplication.class, args);
    }

    @Bean
    public Hibernate5Module jsonHibernate5Module() {
        return new Hibernate5Module();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
