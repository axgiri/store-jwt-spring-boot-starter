package tech.axgiri.jwtstore.spring;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.starter")
public class JwtProperties {
    
    private String issuer;

    private URI jwksUri;

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public URI getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(URI jwksUri) {
        this.jwksUri = jwksUri;
    }

}
