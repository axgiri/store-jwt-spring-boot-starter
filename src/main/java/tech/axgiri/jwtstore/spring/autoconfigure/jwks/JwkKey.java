package tech.axgiri.jwtstore.spring.autoconfigure.jwks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwkKey(
    String kty,
    String kid,
    String use,
    String n,
    String e) {}
