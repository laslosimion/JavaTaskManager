package com.example.barricade.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtils {
    public static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
