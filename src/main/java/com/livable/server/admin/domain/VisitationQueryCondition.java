package com.livable.server.admin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VisitationQueryCondition {
    COMPANY, VISITOR;
}
