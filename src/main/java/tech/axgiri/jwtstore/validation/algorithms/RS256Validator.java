package tech.axgiri.jwtstore.validation.algorithms;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

import tech.axgiri.jwtstore.common.dto.AlgorithmEnum;
import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tech.axgiri.jwtstore.common.exception.ExpiredJwtException;
import tech.axgiri.jwtstore.common.exception.InvalidIssuerException;
import tech.axgiri.jwtstore.common.exception.InvalidSignatureException;
import tech.axgiri.jwtstore.validation.AlgorithmStrategy;

public class RS256Validator implements AlgorithmStrategy {

    private final String expectedIssuer;

    public RS256Validator(String expectedIssuer) {
        this.expectedIssuer = expectedIssuer;
    }

    @Override
    public AlgorithmEnum getAlgorithm() {
        return AlgorithmEnum.RS256;
    }

    @Override
    public void validate(Header header, Payload payload, RawSignature signature, String publicKey) {
        validateAlgorithm(header.alg());
        validateExpiration(payload.exp());
        validateIssuer(payload.iss());
        verifySignature(header, payload, signature, publicKey);
    }

    private void validateAlgorithm(String alg) throws InvalidSignatureException {
        if (!getAlgorithm().name().equals(alg)) {
            throw new InvalidSignatureException("Unsupported algorithm: " + alg);
        }
    }

    private void validateExpiration(long exp) throws ExpiredJwtException {
        if (exp < Instant.now().getEpochSecond()) {
            throw new ExpiredJwtException("Token has expired");
        }
    }

    private void validateIssuer(String iss) throws InvalidIssuerException {
        if (!expectedIssuer.equals(iss)) {
            throw new InvalidIssuerException("Invalid issuer: " + iss);
        }
    }

    private void verifySignature(Header header, Payload payload, RawSignature signature, String publicKey)
            throws InvalidSignatureException {

        String dataToVerify = header + "." + payload;

        boolean isValid = verify(dataToVerify, signature.hash(), publicKey);

        if (!isValid) {
            throw new InvalidSignatureException("Invalid signature");
        }
    }

    private boolean verify(String data, String signature, String publicKey) {
        try {
            PublicKey publicKeyObj = parsePublicKey(publicKey);
            byte[] decodedSignature = Base64.getUrlDecoder().decode(signature);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKeyObj);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            return sig.verify(decodedSignature);

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }

    private PublicKey parsePublicKey(String publicKeyPem) throws Exception {
        String key = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
