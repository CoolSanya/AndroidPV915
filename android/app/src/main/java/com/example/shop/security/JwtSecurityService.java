package com.example.shop.security;

public interface JwtSecurityService {
    void saveJwtToken(String token);
    String getToken();
    void deleteToken();
}
