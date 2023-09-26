package com.livable.server.core.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "png", "gif", "jpeg");
    private static final String EXTENSION_SEPARATOR = ".";

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> saveFile(List<MultipartFile> files) throws IOException {

        List<String> accessUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String accessUrl = saveFile(file);
            accessUrls.add(accessUrl);
        }
        return accessUrls;
    }

    public String saveFile(MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        final String originalFileName = filename.replaceAll(ImageSeparator.IMAGE_SEPARATOR, "");

        final String fileExtension = getFileExtension(originalFileName);

        validationAllowedFileExtension(fileExtension);
        String randomFileName = generateRandomFileName(originalFileName, fileExtension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, randomFileName, file.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, randomFileName).toString();
    }

    // 랜덤한 파일 이름을 생성하는 메서드
    private String generateRandomFileName(String originalFileName, String fileExtension) {
        return UUID.randomUUID() + originalFileName + EXTENSION_SEPARATOR + fileExtension;
    }

    // 파일 이름에서 파일 확장자를 추출하는 메서드
    private String getFileExtension(String originalFileName) {
        return originalFileName
                .substring(originalFileName.lastIndexOf(EXTENSION_SEPARATOR) + 1)
                .toLowerCase();

    }

    // 파일 확장자를 검증하는 메서드
    private void validationAllowedFileExtension(String fileExtension) {
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalArgumentException("지원하지 않는 파일 확장자 입니다.");
        }
    }
}
