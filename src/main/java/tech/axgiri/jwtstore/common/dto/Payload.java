package tech.axgiri.jwtstore.common.dto;

public record Payload(
    String iss,
    long iat,
    long exp,
    String sub,
    String roles) {}
