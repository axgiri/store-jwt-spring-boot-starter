package tech.axgiri.jwtstore.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Header(String alg, String typ) {}
