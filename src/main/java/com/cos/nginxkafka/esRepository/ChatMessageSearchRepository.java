//package com.cos.nginxkafka.esRepository;
//
//import com.cos.nginxkafka.es.ChatMessageIndex;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ChatMessageSearchRepository extends ElasticsearchRepository<ChatMessageIndex, String> {
//    List<ChatMessageIndex> findByChatroomIdAndContentContaining(String chatroomId,String keyword, Sort sort);
//
//}
