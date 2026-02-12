package tech.axgiri.jwtstore.validation.algorithms;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.time.Instant;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.axgiri.jwtstore.common.dto.AlgorithmEnum;
import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tech.axgiri.jwtstore.common.exception.ExpiredJwtException;
import tech.axgiri.jwtstore.common.exception.InvalidIssuerException;
import tech.axgiri.jwtstore.common.exception.InvalidSignatureException;
import tech.axgiri.jwtstore.common.exception.UnsupportedAlgorithmException;
import tech.axgiri.jwtstore.validation.AlgorithmStrategy;
import tools.jackson.databind.ObjectMapper;

public class RS256Validator implements AlgorithmStrategy {

    private static final Logger log = LoggerFactory.getLogger(RS256Validator.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String expectedIssuer;

    public RS256Validator(String expectedIssuer) {
        this.expectedIssuer = expectedIssuer;
    }

    @Override
    public AlgorithmEnum getAlgorithm() {
        return AlgorithmEnum.RS256;
    }

    @Override
    public void validate(String rawHeader, String rawPayload, RawSignature signature, PublicKey publicKey) {
        Header header = decodeHeader(rawHeader);
        Payload payload = decodePayload(rawPayload);
        
        validateAlgorithm(header.alg());
        validateExpiration(payload.exp());
        validateIssuer(payload.iss());
        verifySignatureRaw(rawHeader, rawPayload, signature, publicKey);
    }

    private void validateAlgorithm(String alg) throws UnsupportedAlgorithmException {
        if (!getAlgorithm().name().equals(alg)) {
            throw new UnsupportedAlgorithmException("Unsupported algorithm: " + alg);
        }
    }

    private void validateExpiration(long exp) throws ExpiredJwtException {
        if (exp < Instant.now().getEpochSecond()) {
            throw new ExpiredJwtException("Token has expired");
        }
    }

    private void validateIssuer(String iss) throws InvalidIssuerException {
        if (!expectedIssuer.equals(iss)) {
            log.debug("JWT issuer mismatch: expected={}, actual={}", expectedIssuer, iss);
            throw new InvalidIssuerException("Invalid issuer: " + iss);
        }
    }

    private void verifySignatureRaw(String rawHeader, String rawPayload, RawSignature signature, PublicKey publicKey) throws InvalidSignatureException {
        String dataToVerify = rawHeader + "." + rawPayload;

        boolean isValid = verify(dataToVerify, signature.value(), publicKey);

        if (!isValid) {
            throw new InvalidSignatureException("Invalid signature");
        }
    }

    private Header decodeHeader(String rawHeader) {
        byte[] decoded = Base64.getUrlDecoder().decode(rawHeader);
        try {
            return OBJECT_MAPPER.readValue(decoded, Header.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode header", e);
        }
    }

    private Payload decodePayload(String rawPayload) {
        byte[] decoded = Base64.getUrlDecoder().decode(rawPayload);
        try {
            return OBJECT_MAPPER.readValue(decoded, Payload.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode payload", e);
        }
    }

    private boolean verify(String data, String signature, PublicKey publicKey) {
        try {
            byte[] decodedSignature = Base64.getUrlDecoder().decode(signature);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            return sig.verify(decodedSignature);

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }
}
