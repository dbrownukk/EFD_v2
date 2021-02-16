package efd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = {"com.openxava.naviox.model","efd.model"})

public class Spring5MvcRestApplication {

	public static void main(String[] args) {
		run(Spring5MvcRestApplication.class, args);
	}
}
