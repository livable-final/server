package com.livable.server.core.util;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        String secretKey = "di0xNUNaQDR1MWksaXM4MH5rdSZYLEM2I3dbR0ZQcWJUOVl5UFhmOV52cEROLmE0bCZheHdWLztCZHJoVjwz";

        return new JwtTokenProvider(secretKey);
    }

}
