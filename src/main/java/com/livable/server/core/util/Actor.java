package com.livable.server.core.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Actor {

    private Long id;
    private ActorType actorType;
}
