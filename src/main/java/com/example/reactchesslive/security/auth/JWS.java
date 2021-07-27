package com.example.reactchesslive.security.auth;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWS {
    private String jwt;
    private Date expirationDate;

    public JWS() {
    }

    public JWS(String jwt, Date expirationDate) {
        this.jwt = jwt;
        this.expirationDate = expirationDate;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
