package efd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.openxava.naviox.model")
public class Spring5MvcRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring5MvcRestApplication.class, args);
	}
}
