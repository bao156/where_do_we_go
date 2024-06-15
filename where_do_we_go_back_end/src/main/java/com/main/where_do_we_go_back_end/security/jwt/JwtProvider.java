package com.main.where_do_we_go_back_end.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.main.where_do_we_go_back_end.security.services.UserPrinciple;

// class to generate jwt token:
@Component
public class JwtProvider {

  private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expirationms}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {
    var expirationTime = Instant.now().plus(jwtExpirationMs, ChronoUnit.MILLIS);
    var expirationDate = Date.from(expirationTime);
    var userPrincipal = (UserPrinciple) authentication.getPrincipal();
    Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    return Jwts.builder()
        .claim("sub", userPrincipal.getUsername())
        .setExpiration(expirationDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    var secretBytes = jwtSecret.getBytes();
    var jwsClaims = Jwts.parserBuilder()
        .setSigningKey(secretBytes)
        .build()
        .parseClaimsJws(token);
    return jwsClaims.getBody()
        .getSubject();
  }

  public boolean validateJwtToken(String token) {

    try {
      var secretBytes = jwtSecret.getBytes();
      Jwts.parserBuilder().setSigningKey(secretBytes).build().parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

}
