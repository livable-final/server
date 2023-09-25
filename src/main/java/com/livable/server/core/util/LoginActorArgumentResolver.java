package com.livable.server.core.util;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Slf4j
public class LoginActorArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider tokenProvider;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginActor.class) && parameter.getParameterType().equals(Actor.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        String token = authorization.split("Bearer ")[1];

        Claims claims = tokenProvider.parseClaims(token);
        Long actorId = claims.get("actorId", Long.class);
        String actorType = claims.get("actorType", String.class);

        return Actor.builder()
                .id(actorId)
                .actorType(ActorType.of(actorType))
                .build();
    }
}
