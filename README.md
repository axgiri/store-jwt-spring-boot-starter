# JWT Spring Boot Starter

A lightweight Spring Boot starter library for JWT authentication in microservices. Validates RS256-signed tokens against a JWKS endpoint. Resource server pattern only — verification, no token generation.

## Status

**Private artifact.** Not published to public repositories. Use with Nexus or local Maven repository.

## Quick Start

### 1. Configure application.yml

```yaml
jwt:
  starter:
    enabled: true
    issuer: "https://your-auth-server.com"
    jwks-uri: "https://your-auth-server.com/.well-known/jwks.json"
```

### 2. Access JWT payload in controller

```java
@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        Payload payload = (Payload) request.getAttribute("jwt.payload");
        return ResponseEntity.ok(payload);
    }
}
```

## How It Works

1. On startup, AutoConfig fetches PublicKey from JWKS endpoint
2. For each request with `Authorization: Bearer <token>`:
   - JwtAuthenticationFilter extracts token
   - JwtTokenService parses header, payload, signature
   - RS256Validator verifies signature, expiration, issuer
3. On success, Payload is stored in request attribute `jwt.payload`
4. On failure, returns HTTP 401 with generic error message
   
## JWT Format Expected

**Header:**
```json
{"alg": "RS256", "typ": "JWT"}
```

**Payload:**
```json
{
  "iss": "https://your-auth-server.com",
  "iat": 1739356800,
  "exp": 1739360400,
  "sub": "user-123",
  "roles": "ADMIN,USER"
}
```

Extra claims are allowed and ignored.

## Disable

Set `jwt.starter.enabled: false` in application.yml to disable the filter.

## Build

```bash
./gradlew build
./gradlew publishToMavenLocal
```

## Dependencies

- Java 21+
- Spring Boot 4.0.2
- No Spring Security required

## License

See LICENSE file.
