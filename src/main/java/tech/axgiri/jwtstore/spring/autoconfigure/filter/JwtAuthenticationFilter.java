package tech.axgiri.jwtstore.spring.autoconfigure.filter;

import java.io.IOException;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.axgiri.jwtstore.common.dto.Payload;
import tech.axgiri.jwtstore.common.dto.RawSignature;
import tech.axgiri.jwtstore.token.JwtTokenService;
import tech.axgiri.jwtstore.validation.algorithms.RS256Validator;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_PAYLOAD_ATTRIBUTE = "jwt.payload";

    private final JwtTokenService tokenService;
    private final RS256Validator validator;
    private final PublicKey publicKey;

    public JwtAuthenticationFilter(JwtTokenService tokenService, RS256Validator validator, PublicKey publicKey) {
        this.tokenService = tokenService;
        this.validator = validator;
        this.publicKey = publicKey;
    }

    /*
     * I could make the logic with whitelists to remove the AUTHORIZATION_HEADER
     * check logic and simply pass the request if the endpoint is in the whitelist.
     * But right now I have the task to finish the starter faster.
     * This is an omission in the form of a tradeoff in performance that I allow for
     * now,
     * until the moment comes to grow.
     * Also theoretically this idea can be developed and different algorithm methods
     * can be made,
     * therefore I still keep the strategy pattern logic for the future
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) 
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            String rawHeader = tokenService.getRawHeader(token);
            String rawPayload = tokenService.getRawPayload(token);
            RawSignature signature = tokenService.parseSignature(token);
            validator.validate(rawHeader, rawPayload, signature, publicKey);
            Payload payload = tokenService.parsePayload(token);
            request.setAttribute(JWT_PAYLOAD_ATTRIBUTE, payload);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }
    }
}
