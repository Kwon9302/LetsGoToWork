package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.service.ChatService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DownloadController {
    private final ChatService chatService;

    /**
     * ✅ 파일 다운로드 API
     */
//    @GetMapping("/download")
//    public ResponseEntity<Map<String,String>> downloadFile(@RequestParam("fileName") String fileName) {
//        try {
//            // ✅ Presigned URL 생성 (5분 동안 유효)
//            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                    .bucket("chattingfile") // 버킷 이름
//                    .key(fileName) // 다운로드할 파일 키
//                    .build();
//
//            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                    .signatureDuration(Duration.ofMinutes(5))  // Presigned URL 유효 시간 (5분)
//                    .getObjectRequest(getObjectRequest)
//                    .build();
//
//            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
//            String presignedUrl = presignedRequest.url().toString();
//
//            Map<String, String> response = new HashMap<>();
//            response.put("url", presignedUrl); // 🔹 Presigned URL 반환
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("파일 다운로드 링크 생성 실패", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
