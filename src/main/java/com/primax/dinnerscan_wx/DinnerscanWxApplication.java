package com.primax.dinnerscan_wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DinnerscanWxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DinnerscanWxApplication.class, args);
    }

}
