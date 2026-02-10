package tech.axgiri.jwtstore.rs256;

import tech.axgiri.jwtstore.dto.Header;
import tech.axgiri.jwtstore.dto.Payload;
import tech.axgiri.jwtstore.dto.RawSignature;

public class Entry {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: Entry <publicKey> <rawToken>");
            System.exit(1);
        }

        String publicKey = args[0];
        String rawToken = args[1];

        Service service = new Service();
        Validator validator = new Validator();

        Header header = service.getTokenHeader(rawToken);
        Payload payload = service.getTokenPayload(rawToken);
        RawSignature signature = service.getTokenSignature(rawToken);

        try {
            validator.validate(header, payload, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}