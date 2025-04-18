package pl.kowalecki.dietplannerrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DietPlannerRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DietPlannerRestApiApplication.class, args);
    }

}
