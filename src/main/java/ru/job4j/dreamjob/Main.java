package ru.job4j.dreamjob;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.job4j.dreamjob.config.JdbcConfiguration;

@SpringBootApplication
public class Main {

    @Bean
    public BasicDataSource loadPool() {
        return new JdbcConfiguration().unit();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Go to http://localhost:8080/index");
    }
}