package com.example.plustest.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    // secret key 암호화 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private Key key;

    // bean이 뜰 때 한번만 실행되는 메서드. byte배열에 암호화된 값을 복호화해서 넣고 key에 다시저장
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // jwt 토큰인지 확인하는 메서드
    // 식별자인 "bearer "값이 있는 토큰을 찾고, 그 토큰이 "bearer "로 시작한다면, jwt 토큰
    // 해당토큰의 식별자 값인 "bearer "을 잘라줘서 리턴.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰의 유효성 검사 메서드
    // 토큰이 유효하지 않으면 로그를 찍고 예외를 던짐, 유효하면 true 반환
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 유저 정보를 뽑아오기. 유저정보는 claims에 들어있으므로 claim를 반환
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // jwt 토큰을 만드는 메서드.
    // 토큰은 bearer으로 시작해서 주제는
    public String createToken(String username) {
        Date date = new Date();

        // 토큰 만료시간 60분
        long TOKEN_TIME = 60 * 60 * 1000;
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)   // claims에 정보를 넣어줌(여기서는 유저의 이름)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 토큰 만료시간
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)  //토큰 시크릿키 서명 알고리즘
                        .compact();     ///토큰 생성
    }

}
