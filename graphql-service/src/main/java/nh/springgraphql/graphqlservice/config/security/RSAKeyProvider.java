package nh.springgraphql.graphqlservice.config.security;

import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * <p>
 * GENERATE KEYS WITH OPENSSL:
 * <p>
 * Private Key:  openssl genpkey -out private_key.pem -algorithm RSA -pkeyopt rsa_keygen_bits:4096
 * Public Key :  openssl rsa -pubout -outform pem -in private_key.pem -out public_key.pem
 */
@Component
class RSAKeyProvider {
    private static final Logger log = LoggerFactory.getLogger(RSAKeyProvider.class);

    private final Resource publicKeyResource;
    private final Resource privateKeyResource;

    private RSAKey rsaKey;

    RSAKeyProvider(@Value("${publicKey}") Resource publicKeyResource, @Value("${privateKey}") Resource privateKeyResource) {
        this.publicKeyResource = publicKeyResource;
        this.privateKeyResource = privateKeyResource;
    }

    RSAKey getRsaKey() {
        return rsaKey;
    }

    @PostConstruct
    private void generateRsaKey() throws Exception {

        var publicKeyString = publicKeyResource.getInputStream().readAllBytes();
        var privateKeyString = privateKeyResource.getInputStream().readAllBytes();

        log.debug("Creating RSA KEY......");
        KeyFactory kf = KeyFactory.getInstance("RSA");

        RSAPublicKey publicKey = getPublicKey();
        RSAPrivateKey privateKey = getPrivateKey();

        this.rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
    }

    private RSAPublicKey getPublicKey() throws Exception {
        log.trace("Public key {}", publicKeyResource.getURI());
        byte[] keyBytes = publicKeyResource.getInputStream().readAllBytes();
        String publicKeyPem = new String(keyBytes)
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPem.trim());

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        log.trace("Private key {}", privateKeyResource.getURI());
        byte[] keyBytes = privateKeyResource.getInputStream().readAllBytes();
        String privateKeyPEM = new String(keyBytes)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }
}