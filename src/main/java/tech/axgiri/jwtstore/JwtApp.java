package tech.axgiri.jwtstore;

import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tech.axgiri.jwtstore.token.JwtTokenService;
import tech.axgiri.jwtstore.validation.AlgorithmStrategy;
import tech.axgiri.jwtstore.validation.JwtValidatorFactory;

public class JwtApp {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: JwtApp <publicKey> <rawToken> <expectedIssuer>");
            System.exit(1);
        }

        String publicKey = args[0];
        String rawToken = args[1];
        String expectedIssuer = args[2];

        JwtTokenService tokenService = new JwtTokenService();

        Header header = tokenService.getTokenHeader(rawToken);
        Payload payload = tokenService.getTokenPayload(rawToken);
        RawSignature signature = tokenService.getTokenSignature(rawToken);

        try {
            AlgorithmStrategy validator = JwtValidatorFactory.create(header.alg(), expectedIssuer);
            validator.validate(header, payload, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
