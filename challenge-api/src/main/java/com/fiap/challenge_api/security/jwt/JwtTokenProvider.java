package com.fiap.challenge_api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.challenge_api.dto.TokenDTO;
import com.fiap.challenge_api.service.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    // Config do appplication.properties
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    // Config do appplication.properties
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    // Ação logo após iniciar o Spring
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    // 1. Access Token
    public TokenDTO createAccessToken(String email, List<String> roles) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // Método Access Token
        String accessToken = getAccessToken(email, roles, now, validity);

        // Método Refresh Token
        String refreshToken = getRefreshToken(email, roles, now);

        return new TokenDTO(email, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(String email, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withSubject(email)
                .withIssuer(issuerUrl)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withClaim("typ", "access")
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);
    }

    private String getRefreshToken(String email, List<String> roles, Date now) {
        Date refreshTokenValidity = new Date(now.getTime() + (validityInMilliseconds * 3));

        return JWT.create()
                .withSubject(email)
                .withExpiresAt(refreshTokenValidity)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withClaim("typ", "refresh")
                .withIssuedAt(now)
                .withJWTId(java.util.UUID.randomUUID().toString())
                .sign(algorithm);
    }

    // 2. Refresh Token
    public TokenDTO refreshToken(String refreshToken) {
        var token = "";
        if (tokenContainsBearer(refreshToken)) {
            token = refreshToken.substring("Bearer ".length());
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        List<String> roles = user.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority) // "ROLE_*"
                .toList();

        return createAccessToken(username, roles);
    }

    // 3. Autenticação a partir do Access Token
    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    // 4. Lê Token JWT do Header Authorization
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Bearer {token}
        if (tokenContainsBearer(bearerToken)) return bearerToken.substring("Bearer ".length());

        return null;
    }

    // 5. Valida o Token
    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token!");
        }
    }

    private static boolean tokenContainsBearer(String token) {
        return StringUtils.isNotBlank(token) && token.startsWith("Bearer ");
    }
}
