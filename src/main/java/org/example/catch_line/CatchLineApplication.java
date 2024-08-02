package org.example.catch_line;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatchLineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatchLineApplication.class, args);
	}

}
