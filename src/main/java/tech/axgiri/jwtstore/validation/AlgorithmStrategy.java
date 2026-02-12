package tech.axgiri.jwtstore.validation;

import java.security.PublicKey;

import tech.axgiri.jwtstore.common.dto.AlgorithmEnum;
import tech.axgiri.jwtstore.common.dto.RawSignature;

public interface AlgorithmStrategy {
    
    void validate(String header, String payload, RawSignature signature, PublicKey key);
    
    AlgorithmEnum getAlgorithm();
}
