package com.example.reactchesslive;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Key;


@SpringBootApplication
public class ReactChessliveApplication{

    public static void main(String[] args) {
        SpringApplication.run(ReactChessliveApplication.class, args);
    }
}
