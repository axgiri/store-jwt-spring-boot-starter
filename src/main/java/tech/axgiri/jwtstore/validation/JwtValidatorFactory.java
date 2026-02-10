package tech.axgiri.jwtstore.validation;

import tech.axgiri.jwtstore.common.dto.AlgorithmEnum;
import tech.axgiri.jwtstore.common.exception.UnsupportedAlgorithmException;
import tech.axgiri.jwtstore.validation.algorithms.RS256Validator;

public class JwtValidatorFactory {

    public static AlgorithmStrategy create(String algorithm, String expectedIssuer) {
        try {
            AlgorithmEnum alg = AlgorithmEnum.valueOf(algorithm);
            return createByEnum(alg, expectedIssuer);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedAlgorithmException("Unknown algorithm: " + algorithm);
        }
    }

    /**
     * Maps AlgorithmEnum values to their corresponding validators
     */
    private static AlgorithmStrategy createByEnum(AlgorithmEnum algorithm, String expectedIssuer) {
        return switch (algorithm) {
            case RS256 -> new RS256Validator(expectedIssuer);
            default -> throw new UnsupportedAlgorithmException("Algorithm not yet implemented: " + algorithm);
        };
    }
}
