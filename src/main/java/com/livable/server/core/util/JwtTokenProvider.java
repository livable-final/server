package com.livable.server.core.util;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.member.domain.MemberErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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

    public static void checkAdminToken(Actor actor) {
        if (actor.getActorType() != ActorType.ADMIN) {
            throw new GlobalRuntimeException(MemberErrorCode.INVALID_TOKEN);
        }
    }

}
