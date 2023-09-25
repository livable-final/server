package com.livable.server.core.util;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.member.domain.MemberErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Actor 정보를 입력하여 JWT 토큰을 생성하는 메서드
     * @param actorType: 엑터의 종류
     * @param actorId: 엑터의 식별값
     * @param expireDate: 토큰의 만료일
     * @return 입력된 정보로 만든 토큰을 반환
     */
    public String createActorToken(ActorType actorType, Long actorId, Date expireDate) {

        Claims claims = Jwts.claims();
        claims.put("actorId", actorId);
        claims.put("actorType", actorType);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰의 유효성을 검사하는 메서드
     * @param token: JWT 토큰 입력
     * @return 입력한 토큰이 유효하면 true, 유효하지 않으면 false
     */
    public boolean isValidateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            e.printStackTrace();
            throw new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN);
        }
    }

    public static void checkMemberToken(Actor actor) {
        if (actor.getActorType() != ActorType.MEMBER) {
            throw new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN);
        }
    }

    public static void checkVisitorToken(Actor actor) {
        if (actor.getActorType() != ActorType.VISITOR) {
            throw new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN);
        }
    }

}
