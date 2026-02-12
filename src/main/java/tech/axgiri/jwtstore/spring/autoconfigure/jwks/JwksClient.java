package tech.axgiri.jwtstore.spring.autoconfigure.jwks;

import java.math.BigInteger;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

public class JwksClient {

    private static final Logger log = LoggerFactory.getLogger(JwksClient.class);
    private final RestClient restClient;

    public JwksClient() {
        this.restClient = RestClient.create();
    }

    public PublicKey fetchPublicKey(URI jwksUri) {
        log.debug("Fetching JWKS from {}", jwksUri);

        JwksResponse response = restClient.get()
                .uri(jwksUri)
                .retrieve()
                .body(JwksResponse.class);

        if (response == null) {
            throw new IllegalStateException("JWKS response is null from " + jwksUri);
        }

        if (keySetIsEmpty(response)) {
            throw new IllegalStateException("JWKS response does not contain any valid keys");
        }

        JwkKey key = response.keys().get(0);
        return buildPublicKey(key.n(), key.e());
    }

    private boolean keySetIsEmpty(JwksResponse response) {
        return response.keys() == null || response.keys().isEmpty() || response.keys().get(0).n() == null || response.keys().get(0).e() == null;
    }

    private PublicKey buildPublicKey(String n, String e) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(n);
            byte[] eBytes = Base64.getUrlDecoder().decode(e);
            BigInteger modulus  = new BigInteger(1, nBytes);
            BigInteger exponent = new BigInteger(1, eBytes);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(spec);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to build public key from JWKS", ex);
        }
    }
}
