package tech.axgiri.jwtstore.rs256;

import tech.axgiri.jwtstore.Decoder;
import tech.axgiri.jwtstore.dto.Header;
import tech.axgiri.jwtstore.dto.Payload;
import tech.axgiri.jwtstore.dto.RawSignature;

public class Service {

    public Header getTokenHeader(String rawToken) {
        return Decoder.decodeTo(getRawHeader(rawToken), Header.class);
    }

    public Payload getTokenPayload(String rawToken) {
        return Decoder.decodeTo(getRawPayload(rawToken), Payload.class);
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
}
