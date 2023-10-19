package com.livable.server.core.util;

import com.livable.server.core.exception.GlobalErrorCode;
import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

@Slf4j
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source);
        } catch (RuntimeException e) {
            throw new GlobalRuntimeException(GlobalErrorCode.INVALID_TYPE);
        }
    }
}
