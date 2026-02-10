package tech.axgiri.jwtstore.validation;

import tech.axgiri.jwtstore.common.dto.AlgorithmEnum;
import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;

public interface AlgorithmStrategy {
    
    void validate(Header header, Payload payload, RawSignature signature, String key);
    
    AlgorithmEnum getAlgorithm();
}
