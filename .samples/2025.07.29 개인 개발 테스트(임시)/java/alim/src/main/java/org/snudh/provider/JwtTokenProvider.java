package org.snudh.provider;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.crypto.SecretKey;

import org.snudh.domain.PatInfoVO;
import org.snudh.domain.RsvInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  private SecretKey key;
  private JwtParser jwtParser;
  private String header;
  private ZoneId zone;

  public JwtTokenProvider(
    @Value("${security.jwt.token.secret-key}") final String secretKey,
    @Value("${security.jwt.token.heaer}") final String header
  ) {
    this.key = getKeyFromBase64EncodedKey(secretKey);
    this.header = header + ".";
    jwtParser = Jwts.parser().verifyWith(key).build();
    this.zone = ZoneId.systemDefault();
  }

  public String createToken(RsvInfoDto ri) {
    String token = Jwts.builder()
          .header().empty().and()
          .claim("ptn", ri.getPatno())
          .claim("mdt", ri.getRsvdate().atZone(zone).toEpochSecond())
          .claim("mdd", ri.getMeddept())
          //.claim("iat", LocalDateTime.now().atZone(zone).toEpochSecond())
          //claim("exp", ri.getRsvdate().toLocalDate().plusMonths(6).plusDays(1).atStartOfDay(zone).toEpochSecond())
          .signWith(key)
          .compact();
    if (token.startsWith(header))
      return token.substring(header.length());
    return null;
  }

  public PatInfoVO getAccessTokenInfo(String token) {
    Claims claims = jwtParser.parseSignedClaims(header + token).getPayload();
    //PatToken patToken = patTokenRepository.findBy
    PatInfoVO pi = new PatInfoVO();
    long _mdt   = claims.get("mdt", Long.class);
    LocalDateTime mdt = Instant.ofEpochSecond(_mdt).atZone(zone).toLocalDateTime();
    //LocalDate exp = mdt.toLocalDate().plusMonths(6).plusDays(1);

    String ptn  = claims.get("ptn", String.class);
    String mdd  = claims.get("mdd", String.class);
    pi.setPatno(ptn);
    pi.setMeddate(mdt);
    pi.setMeddept(mdd);

    return pi;
  }

  private static SecretKey getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

    // key byte array를 기반으로 적절한
    // HMAC 알고리즘을 적용한 Key(java.security.Key) 객체를 생성
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
