package com.example.reactchesslive.security.auth;

import org.springframework.stereotype.Component;

@Component
public class JWS {
    private String jwt;

    public JWS() {
    }
    public JWS(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
