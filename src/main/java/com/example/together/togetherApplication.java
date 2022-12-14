package com.example.together;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class togetherApplication {
  public static void main(String[] args) {
    SpringApplication.run(togetherApplication.class, args);
  }
}
