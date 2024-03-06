package org.example.kcu_website.entity;

import org.example.kcu_website.model.User;
import org.example.kcu_website.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class DataInitializer {
//    @Bean
//    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            User user = new User();
//            user.setUsername("kcuadmin");
//            user.setPassword(passwordEncoder.encode("Kcu!Sp24Admin@"));
//            userRepository.save(user);
//        };
//    }
}
