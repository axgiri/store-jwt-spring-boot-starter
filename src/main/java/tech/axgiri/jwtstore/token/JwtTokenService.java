package tech.axgiri.jwtstore.token;

import java.util.Base64;

import tech.axgiri.jwtstore.common.dto.Header;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tools.jackson.databind.ObjectMapper;
public class JwtTokenService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Header parseHeader(String rawToken) {
        return decodeTo(getRawHeader(rawToken), Header.class);
    }

    public Payload parsePayload(String rawToken) {
        return decodeTo(getRawPayload(rawToken), Payload.class);
    }

    public RawSignature parseSignature(String rawToken) {
        return getSignature(rawToken);
    }

    public String getRawHeader(String rawToken) {
        int indexOfFirstDot = indexOfDot(rawToken, 0);
        String rawHeader = rawToken.substring(0, indexOfFirstDot);
        return rawHeader;
    }

    public String getRawPayload(String rawToken) {
        int indexOfFirstDot = indexOfDot(rawToken, 0);
        int indexOfSecondDot = indexOfDot(rawToken, indexOfFirstDot + 1);
        String rawPayload = rawToken.substring(indexOfFirstDot + 1, indexOfSecondDot);
        return rawPayload;
    }

    private RawSignature getSignature(String rawToken) {
        int indexOfSecondDot = indexOfDot(rawToken, indexOfDot(rawToken, 0) + 1);
        String signature = rawToken.substring(indexOfSecondDot + 1);
        return new RawSignature(signature);
    }

    private <T> T decodeTo(String base64Url, Class<T> type) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(base64Url);
            return OBJECT_MAPPER.readValue(decoded, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode JWT segment", e);
        }
    }

    private int indexOfDot(String str, int fromIndex) {
        for (int i = fromIndex; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid JWT format: token must contain header, payload, and signature separated by dots");
    }
}
