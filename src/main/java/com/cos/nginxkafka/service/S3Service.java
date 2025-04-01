package com.cos.nginxkafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    /**
     * 채팅에서 파일을 업로드하는 메서드
     *
     * @param file
     * @return
     * @throws FileUploadException
     */
    public String uploadFile(MultipartFile file) throws FileUploadException {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 📌 UUID + 확장자로 안전한 파일명 생성
            String safeFileName = UUID.randomUUID().toString() + extension;

            log.info("✅ 업로드 요청: 원래 파일명 = {}", originalFilename);
            log.info("✅ 변환된 파일명 = {}", safeFileName);
            log.info("✅ 업로드 요청: 버킷 = {}", bucketName);

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(safeFileName)
                    .contentType(file.getContentType())
                    .build();

            log.info("Presigned URL 요청 생성 시작");

            // Presigned URL 요청 생성 (유효시간 5분)
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .putObjectRequest(putObjectRequest)
                    .build();

            log.info("Presigned URL 생성 시작");

            // Presigned URL 생성
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            log.info("Presigned URL: {}", presignedRequest.url());

            // 파일을 byte[]로 읽어서 Content-Length 헤더를 설정
            byte[] fileBytes = file.getBytes();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(presignedRequest.url().toString()))
                    .header("Content-Type", file.getContentType())
                    .PUT(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                    .build();
            log.info("HTTP PUT 요청 실행");

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.info("응답 상태 코드: {}", response.statusCode());

            if (response.statusCode() == 200) {
                log.info("✅ S3 업로드 성공: {}", safeFileName);
                return safeFileName; // ✅ 변환된 파일명을 반환
            } else {
                throw new FileUploadException("파일 업로드 실패, 상태 코드: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new FileUploadException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 파일이 저장된 S3의 URL 생성
     * @param fileName
     * @return
     */
    public String generatePresignedUrl(String fileName) {
            log.info("🔗 Presigned URL 생성 요청: {}", fileName);

            // Presigned URL 요청 생성 (유효기간: 5분)
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .getObjectRequest(r -> r.bucket(bucketName).key(fileName))
                    .build();

            // Presigned URL 생성
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();

            log.info("생성된 URL : {}", presignedUrl);
            return presignedUrl;
    }
}
