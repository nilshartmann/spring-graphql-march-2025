package nh.springgraphql.graphqlservice.config.security;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Generates an almost never expiring token for testing.
 *
 * 👮  👮  👮 YOU WILL NEVER DO THIS IN PRODUCTION 👮  👮  👮
 *
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
@Configuration
@ConditionalOnProperty("demo-token-generator.enable")
public class DemoTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(DemoTokenGenerator.class);

    private final JwtEncoder encoder;

    private final Map<String, String> tokens;

    public DemoTokenGenerator(JwtEncoder encoder) {
        this.encoder = encoder;
        this.tokens = Map.of(
            "user1", generateToken("user1", "ROLE_USER"),
            "admin1", generateToken("admin1", "ROLE_ADMIN")
        );
    }


    /**
     * Creates a token that will last a couple of years(!) and will be stable accross re-starts
     * as longs as the RSAKey does not change (keys from publicKey and privateKey application properties)
     * <p>
     * This token can be used for easier testing using command line tools etc.
     * 👮  👮  👮 YOU SHOULD NEVER DO THIS IN 'REAL' PRODUCTION APPS 👮  👮  👮
     */
    private String generateToken(String username, String role) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(username)
            .claim("roles", role)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(10 * 365, ChronoUnit.DAYS))
            .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @PostConstruct
    void dumpTokens() {
        log.info("""
===============================================================
🚨 🚨 🚨 NEVER EXPIRING JWT TOKENS 🚨 🚨 🚨
===============================================================
USER-1:

Authorization: Bearer {}

Für 'headers' in GraphiQL:

{ "Authorization": "Bearer {}" }

ADMIN-1:

Authorization: Bearer {}

Für 'headers' in GraphiQL:

{ "Authorization": "Bearer {}" }

===============================================================""",
            tokens.get("user1"),
            tokens.get("user1"),
            tokens.get("admin1"),
            tokens.get("admin1"));
    }
}