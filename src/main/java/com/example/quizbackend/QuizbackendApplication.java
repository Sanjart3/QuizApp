package com.example.quizbackend;

import com.example.quizbackend.admin.Admin;
import com.example.quizbackend.admin.AdminRepository;
import com.example.quizbackend.admin.AdminService;
import com.example.quizbackend.general.ERole;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class QuizbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizbackendApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}


