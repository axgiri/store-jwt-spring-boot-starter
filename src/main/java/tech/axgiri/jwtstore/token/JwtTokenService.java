package tech.axgiri.jwtstore.token;

import java.util.Base64;

import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tools.jackson.databind.ObjectMapper;

public class JwtTokenService {

    public Header getTokenHeader(String rawToken) {
        return decodeTo(getRawHeader(rawToken), Header.class);
    }

    public Payload getTokenPayload(String rawToken) {
        return decodeTo(getRawPayload(rawToken), Payload.class);
    }

    public RawSignature getTokenSignature(String rawToken) {
        return getSignature(rawToken);
    }

    private String getRawHeader(String rawToken) {
        int indexOfFirstDot = rawToken.indexOf(".");
        String rawHeader = rawToken.substring(0, indexOfFirstDot);
        return rawHeader;
    }

    private String getRawPayload(String rawToken) {
        int indexOfFirstDot = rawToken.indexOf(".");
        int indexOfSecondDot = rawToken.indexOf(".", indexOfFirstDot + 1);
        String rawPayload = rawToken.substring(indexOfFirstDot + 1, indexOfSecondDot);
        return rawPayload;
    }

    private RawSignature getSignature(String rawToken) {
        int indexOfSecondDot = rawToken.lastIndexOf(".");
        String signature = rawToken.substring(indexOfSecondDot + 1);
        return new RawSignature(signature);
    }

    private <T> T decodeTo(String base64Url, Class<T> type) {
        byte[] decoded = Base64.getUrlDecoder().decode(base64Url);
        ObjectMapper om = new ObjectMapper();
        return om.readValue(decoded, type);
    }
}
