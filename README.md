# Store JWT Starter ([axgiri.tech](https://axgiri.tech/about))

store_jwt_starter is an internal shared library for JWT validation in platform microservices. It provides one consistent token verification path and removes duplicated security implementation across services. This module follows a resource server pattern and focuses on verification only.

## Status

Private artifact for the axgiri.tech ecosystem. It is intended for private Nexus or local Maven repositories.

### Role in the architecture

store_jwt_starter is the trust adapter between token issuer and token consumers. It fetches public keys through JWKS, validates token signature and claims, and injects parsed payload into request context for business processing.

It is used by service modules that need authenticated request context but do not need to implement full auth server behavior.

### Functional scope

- reads bearer token from incoming request headers
- parses JWT header, payload, and signature
- validates RS256 signature using JWKS sourced public key
- validates issuer and expiration claims
- exposes parsed payload as request attribute for downstream logic
- supports autoconfiguration toggle through `jwt.starter.enabled`

### Quick configuration

```yaml
jwt:
  starter:
    enabled: true
    issuer: "https://your-auth-server.com"
    jwks-uri: "https://your-auth-server.com/.well-known/jwks.json"
```

### Access payload in controller

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

### How validation works

1. On startup, autoconfiguration loads PublicKey from the configured JWKS endpoint
2. For each request with `Authorization: Bearer <token>`
   - `JwtAuthenticationFilter` extracts token
   - `JwtTokenService` parses header, payload, and signature
   - `RS256Validator` validates signature, expiration, and issuer
3. On success, payload is stored in request attribute `jwt.payload`
4. On failure, the request is rejected with HTTP 401

### JWT format expected

Header:

```json
{"alg": "RS256", "typ": "JWT"}
```

Payload example:

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

### Data and integrations

- integrates with auth JWKS endpoint for key discovery
- plugs into Spring Boot auto configuration lifecycle
- standardizes JWT validation behavior for store_core and store_chat

### Tech Stack

- Java 21
- Spring Boot AutoConfiguration
- Servlet filter based request interception
- RS256 JWT signature validation
- JWKS public key discovery
- Gradle Kotlin DSL and maven-publish

### Platform impact

store_jwt_starter keeps authentication trust semantics identical across services and reduces long term security drift.

## Publishing to GitHub Packages

This project is configured to publish artifacts to GitHub Packages.
To publish a new version, execute the following command:

```shell
./gradlew publish
```

Authentication is required. Ensure that the `~/.gradle/gradle.properties` file on the build machine contains valid GitHub credentials:
```properties
gpr.user=github_username
gpr.key=github_personal_access_token_with_write_packages_scope
```
For CI/CD environments, GitHub Actions can use the standard environment variables (`GITHUB_ACTOR` and `GITHUB_TOKEN`).

## All microservices

- https://github.com/axgiri/store-jwt-spring-boot-starter
- https://github.com/axgiri/store_gateway
- https://github.com/axgiri/store_infrastructure
- https://github.com/axgiri/store_auth
- https://github.com/axgiri/store_core
- https://github.com/axgiri/store_chat
- https://github.com/Scheldie/Notification_Reports