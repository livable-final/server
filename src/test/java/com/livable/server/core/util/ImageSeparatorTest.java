package com.livable.server.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ImageSeparatorTest {

    private ImageSeparator imageSeparator;

    @BeforeEach
    void setUp() {
        imageSeparator = new ImageSeparator();
    }

    @Test
    void success_Test_GivenMultipleUrls() {
        // Given
        String images = "https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95%ED%83%95.jpg,https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95.jpg";

        // When
        List<String> actual = imageSeparator.separateConcatenatedImages(images);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, actual.size()),
                () -> Assertions.assertEquals("https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95%ED%83%95.jpg", actual.get(0)),
                () -> Assertions.assertEquals("https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95.jpg", actual.get(1))
        );
    }

    @Test
    void success_Test_GivenSingleUrls() {
        // Given
        String images = "https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95%ED%83%95.jpg";

        // When
        List<String> actual = imageSeparator.separateConcatenatedImages(images);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, actual.size()),
                () -> Assertions.assertEquals("https://livable-final.s3.ap-northeast-2.amazonaws.com/%EB%A7%88%EB%9D%BC%ED%83%95%ED%83%95.jpg", actual.get(0))
        );
    }

    @Test
    void success_Test_GivenNull() {
        // Given
        String images = null;

        // When
        List<String> actual = imageSeparator.separateConcatenatedImages(images);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertTrue(actual.isEmpty())
        );
    }
}
