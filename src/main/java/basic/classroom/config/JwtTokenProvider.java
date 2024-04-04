package basic.classroom.config;

import basic.classroom.service.MemberDetailsService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    private final long tokenValidTime = 1000L * 60 * 60; // 1H
    private final MemberDetailsService memberDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String loginId) {
        Claims claims = Jwts.claims().setSubject(loginId);
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String loginId) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String getLoginId(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String getJwt(HttpServletRequest request) {
        String bearerToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    bearerToken = cookie.getValue();
                }
            }
        }
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
