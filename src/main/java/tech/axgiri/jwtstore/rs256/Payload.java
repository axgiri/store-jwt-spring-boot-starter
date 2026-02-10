package tech.axgiri.jwtstore.rs256;

record Payload(
            String iss,
            long iat,
            long exp,
            String sub,
            String roles) {
    }