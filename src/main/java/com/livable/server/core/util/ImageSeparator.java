package com.livable.server.core.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class ImageSeparator {

    public static final String IMAGE_SEPARATOR = ",";

    /**
     * IMAGE_SEPARATOR로 이어진 이미지 url 문자열을 리스트로 분리하여 반환한다.
     * @param concatenatedImageUrl
     * @return 분리된 url 리스트
     */
    public List<String> separateConcatenatedImages(String concatenatedImageUrl) {
        if (Objects.isNull(concatenatedImageUrl)) {
            return new ArrayList<>();
        }
        return Arrays.asList(concatenatedImageUrl.split(IMAGE_SEPARATOR));
    }
}
