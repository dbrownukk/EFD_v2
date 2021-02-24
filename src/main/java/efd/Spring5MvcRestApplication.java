package efd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = {"com.openxava.naviox.model","efd.model"})

public class Spring5MvcRestApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
//		run(Spring5MvcRestApplication.class, args);
        SpringApplication.run(Spring5MvcRestApplication.class, args);
	}
}
