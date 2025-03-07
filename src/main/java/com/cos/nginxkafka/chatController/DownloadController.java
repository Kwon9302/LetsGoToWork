package com.cos.nginxkafka.chatController;

import com.cos.nginxkafka.service.ChatService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/download")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(@RequestParam("fileUrl") String fileUrl) {
        String presignedUrl = chatService.getFileDownloadUrl(fileUrl);
        Map<String, String> response = new HashMap<>();
        response.put("url", presignedUrl);
        return ResponseEntity.ok(response);
    }
}
