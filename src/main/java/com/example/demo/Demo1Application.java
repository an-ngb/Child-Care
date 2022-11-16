package com.example.demo;

import com.cloudinary.Cloudinary;
import com.example.demo.entities.ParentGroup;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.UserProfile;
import com.example.demo.repositories.ParentGroupRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserProfileRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

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
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary;
        Map config = new HashMap();
        config.put("cloud_name", "annb");
        config.put("api_key", "544826756355157");
        config.put("api_secret", "KOi-qJgeNjS8kuWTggHAydUtl3g");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    //	@Bean
//    CommandLineRunner runner(RoleRepository roleRepo, PostRepository postRepository, UserRepository userRepository) {
//		return args -> {
//			Role admin = new Role(1L, "admin");
//			Role doctor = new Role(2L, "doctor");
//			Role user = new Role(3L, "user");
//			roleRepo.save(admin);
//			roleRepo.save(doctor);
//			roleRepo.save(user);
//
//            User user1 = new User("exampleEmail@gmail.com", "$2a$12$Xh19B4FekngEuDuzZAF5v.8JnavUWhu7frbZHFvt1SP1c/8RLwaMq", "An", 22, 2, "0941506499", admin);
//            userRepository.save(user1);
//		};
//	}
    @Bean
    CommandLineRunner runner(RoleRepository roleRepo, UserRepository userRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder, ParentGroupRepository parentGroupRepository) {
        return args -> {
            if (roleRepo.findRoleById(1) == null && roleRepo.findRoleById(2) == null && roleRepo.findRoleById(3) == null) {
                Role admin = new Role(1, "admin");
                Role doctor = new Role(2, "doctor");
                Role user = new Role(3, "user");
                roleRepo.save(admin);
                roleRepo.save(doctor);
                roleRepo.save(user);
            }

            if (userRepository.findByEmail("adminUser@adminUser.com") == null && roleRepo.findRoleById(1) != null) {
                User user1 = new User("adminUser@adminUser.com", passwordEncoder.encode("Admin123!@#"), roleRepo.findRoleById(1));
                UserProfile userProfile = new UserProfile(user1, "Admin", "Ho Chi Minh City", 25, 1, "0912345678");

                userRepository.save(user1);
                userProfileRepository.save(userProfile);
            }

            parentGroupRepository.save(new ParentGroup(152, "Sức khỏe của bé"));
            parentGroupRepository.save(new ParentGroup(153, "Dinh dưỡng cho bé"));
            parentGroupRepository.save(new ParentGroup(154, "Mẹ và bé"));
            parentGroupRepository.save(new ParentGroup(155, "Cẩm nang cho mẹ"));
            parentGroupRepository.save(new ParentGroup(156, "Chuyện chị em"));
        };
    }
}
