package org.chorem.ecd.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Payload;
import com.google.common.base.Strings;
import org.chorem.ecd.dao.PersonDao;
import org.chorem.ecd.dao.TransactionRequired;
import org.chorem.ecd.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Inject
    private PersonDao personDao;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    public AuthService() throws UnsupportedEncodingException {
        algorithm = Algorithm.HMAC512("secretpassphrase");
        verifier = JWT.require(algorithm).withIssuer("auth0").build();
    }

    @TransactionRequired
    public String getSignedToken(String subject, String password) {
        if (Strings.isNullOrEmpty(subject) || Strings.isNullOrEmpty(password)) {
            return null;
        }

        Person person = new Person();
        person.setName("John Doe");
        personDao.addPerson(person);

        Instant now = Instant.now();
        //now = null;
        Instant expirationInstant = Instant.now().plus(Duration.ofMinutes(20));

        String token = JWT.create()
                .withIssuer("auth0")
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expirationInstant))
                .sign(algorithm);

        return token;
    }

    public Payload decodeSignedToken(String signedToken) {
        try {
            Payload verifiedToken = verifier.verify(signedToken);
            return verifiedToken;
        } catch(JWTVerificationException e) {
            return null;
        }
    }

}
