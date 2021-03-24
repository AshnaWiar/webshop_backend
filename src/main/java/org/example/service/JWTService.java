package org.example.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.sql.Date;
import java.time.Instant;

public class JWTService {

    public static final Algorithm algorithm = Algorithm.HMAC256("38%203186!@bf01-4439#^96f!@6b0be%a09df4c");
    public static final JWTVerifier verifier = JWT.require(algorithm).withIssuer("dropwizard").build();
    private static final long JWT_MAX_DURATION = 54000; // 15 minutes

    public String generateToken() {
        return JWT.create()
                .withIssuer("dropwizard")
                .withExpiresAt(Date.from(Instant.now().plusSeconds(JWT_MAX_DURATION)))
                .sign(algorithm);
    }

    public boolean isValid(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

}
