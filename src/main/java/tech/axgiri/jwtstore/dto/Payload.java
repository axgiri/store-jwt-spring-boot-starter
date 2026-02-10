package tech.axgiri.jwtstore.dto;

public record Payload(
            String iss,
            long iat,
            long exp,
            String sub,
            String roles){}
