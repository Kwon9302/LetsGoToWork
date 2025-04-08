//package com.cos.nginxkafka.es;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.DateFormat;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.time.OffsetDateTime;
//
//@Document(indexName = "chatmessages")
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class ChatMessageIndex {
//
//    @Id
//    private String id;
//
//    @Field(type = FieldType.Keyword)
//    private String chatroomId;
//
//    @Field(type = FieldType.Keyword) // sender는 정확한 검색을 위해 keyword 타입으로 변경
//    private String sender;
//
//    @Field(type = FieldType.Text, analyzer = "standard") // content는 부분 검색을 위해 text 타입 유지
//    private String content;
//
//    @Field(type = FieldType.Date, format = DateFormat.date_time)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
//    private OffsetDateTime timestamp;
//}
