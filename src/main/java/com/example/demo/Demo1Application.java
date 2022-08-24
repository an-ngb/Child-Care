package com.example.demo;

import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.entities.Role;
import com.example.demo.repositories.PostRepository;
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
    CommandLineRunner runner(RoleRepository roleRepo, PostRepository postRepository) {
		return args -> {
			Role admin = new Role(1L, "admin");
			Role doctor = new Role(2L, "doctor");
			Role user = new Role(3L, "user");
			roleRepo.save(admin);
			roleRepo.save(doctor);
			roleRepo.save(user);
		};
	}
}
