package com.cos.nginxkafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws FileUploadException {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
            // ✅ S3에 직접 InputStream으로 업로드
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType()) // MIME 타입 설정 (예: image/jpeg)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()) // InputStream 사용
            );

            return fileName;
        } catch (IOException e) {
            throw new FileUploadException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     *  S3에서 파일 다운로드 (AWS SDK v2)
     */
//    public byte[] downloadFile(String fileName) {
//        String filename = fileName.substring(fileName.lastIndexOf('/') + 1);
//
//        S3Object s3Object = s3Client.getObject(bucketName, filename);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//        try {
//            byte[] content = IOUtils.toByteArray(inputStream);
//            return content;
//        } catch (IOException e) {
//            // e.printStackTrace();
//            throw new IllegalStateException("aws s3 다운로드 error");
//        }
//    }
}
