package kg.cs.mobileapp;

import kg.cs.mobileapp.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MobileAppWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileAppWsApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // we created this Bean so we can retrieve the Beans during run-time
    @Bean
    public SpringApplicationContext springApplicationContext(){
        return new SpringApplicationContext();
    }

    @Bean(name="AppProperties")
    public AppProperties getAppProperties(){
        return new AppProperties();
    }
}
