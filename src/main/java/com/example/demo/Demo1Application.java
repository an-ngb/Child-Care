package com.example.demo;

import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new UsernameAuditorAware();
    }

	@Bean
    CommandLineRunner runner(RoleRepository roleRepo) {
		return args -> {
			Role admin = new Role(1, "admin");
			Role doctor = new Role(2, "doctor");
			Role user = new Role(3, "user");
			roleRepo.save(admin);
			roleRepo.save(doctor);
			roleRepo.save(user);

		};
	}
}
