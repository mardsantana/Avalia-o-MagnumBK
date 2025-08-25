package br.com.testmagnumbk.magnum_bk_teste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MagnumBkTesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagnumBkTesteApplication.class, args);
    }
}