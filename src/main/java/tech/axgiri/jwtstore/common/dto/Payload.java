package tech.axgiri.jwtstore.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Payload(
    String iss,
    long iat,
    long exp,
    String sub,
    String roles) {}
