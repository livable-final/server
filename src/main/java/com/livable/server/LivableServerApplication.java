package com.livable.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class LivableServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LivableServerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("프로젝트 세팅 시간: {}", LocalDateTime.now()); // TODO: 서버 로그 테스트 후 지울예정
    }

}
