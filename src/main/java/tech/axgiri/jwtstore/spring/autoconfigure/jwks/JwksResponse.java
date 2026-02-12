package tech.axgiri.jwtstore.spring.autoconfigure.jwks;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwksResponse(List<JwkKey> keys) {}
