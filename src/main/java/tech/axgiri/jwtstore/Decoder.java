package tech.axgiri.jwtstore;

import java.util.Base64;

import tools.jackson.databind.ObjectMapper;

public class Decoder {
    public static <T> T decodeTo(String base64Url, Class<T> type) {
        byte[] decoded = Base64.getUrlDecoder().decode(base64Url);
        ObjectMapper om = new ObjectMapper();
        return om.readValue(decoded, type);
    }
}
