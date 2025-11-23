package com.quiz.app.service;

import com.quiz.app.exception.InvalidJwtException;
import com.quiz.app.exception.JwtInitializationException;
import com.quiz.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

  private String secretkey = "";

  public JWTService() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
      SecretKey sk = keyGen.generateKey();
      secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new JwtInitializationException(
        "Failed to initialize JWT secret key",
        e
      );
    }
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts
      .builder()
      .claims()
      .add(claims)
      .subject(user.getUsername())
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
      .and()
      .signWith(getKey())
      .compact();
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretkey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    // extract the username from jwt token
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
        .parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (ExpiredJwtException e) {
      throw new InvalidJwtException("JWT token has expired", e);
    } catch (MalformedJwtException e) {
      throw new InvalidJwtException("Invalid JWT token", e);
    } catch (Exception e) {
      throw new InvalidJwtException("Failed to parse JWT token", e);
    }
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (
      userName.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
