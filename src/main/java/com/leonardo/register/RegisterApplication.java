package com.leonardo.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
public class RegisterApplication {

    private static final ZoneId AMERICAS_SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    Clock systemClock() {
        return Clock.system(AMERICAS_SAO_PAULO_ZONE);
    }

}
