package com.livable.server.core.util;

import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@TestPropertySource(locations = "classpath:test.properties")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestProperties {
}
