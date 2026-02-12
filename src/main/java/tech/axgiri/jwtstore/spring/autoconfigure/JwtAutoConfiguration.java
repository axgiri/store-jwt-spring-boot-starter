package tech.axgiri.jwtstore.spring.autoconfigure;

import java.security.PublicKey;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import tech.axgiri.jwtstore.spring.JwtProperties;
import tech.axgiri.jwtstore.spring.autoconfigure.filter.JwtAuthenticationFilter;
import tech.axgiri.jwtstore.spring.autoconfigure.jwks.JwksClient;
import tech.axgiri.jwtstore.token.JwtTokenService;
import tech.axgiri.jwtstore.validation.algorithms.RS256Validator;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt.starter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtAutoConfiguration {

    @Bean
    public JwksClient jwksClient() {
        return new JwksClient();
    }

    @Bean
    public PublicKey jwtPublicKey(JwksClient client, JwtProperties props){
        return client.fetchPublicKey(props.getJwksUri());
    }

    @Bean
    public RS256Validator rs256Validator(JwtProperties props){
        return new RS256Validator(props.getIssuer());
    }

    @Bean
    public JwtTokenService jwtTokenService(){
        return new JwtTokenService();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService tokenService, RS256Validator validator, PublicKey publicKey) {
        return new JwtAuthenticationFilter(tokenService, validator, publicKey);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}