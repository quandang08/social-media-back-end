package com.socialmedia.backend.service;

import com.socialmedia.backend.config.GeminiConfig;
import com.socialmedia.backend.response.GeminiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class GeminiService {
    @Autowired
    private GeminiConfig geminiConfig;

    @Autowired
    private RestTemplate restTemplate;

    public String getChatCompletion(String prompt) {
        //Khởi tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", geminiConfig.getKey());

        //Tạo request body
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();

        Map<String, Object> contentData = new HashMap<>();
        contentData.put("role", "user");

        List<Map<String, String>> parts = new ArrayList<>();
        parts.add(Map.of("text", prompt));
        contentData.put("parts", parts);

        contents.add(contentData);
        requestBody.put("contents", contents);

        //Gửi request
        GeminiResponse response = restTemplate.postForObject(
                geminiConfig.getUrl(),
                new HttpEntity<>(requestBody, headers),
                GeminiResponse.class
        );

        //Xử lý response
        return Optional.ofNullable(response)
                .map(GeminiResponse::getCandidates)
                .filter(c -> !c.isEmpty())
                .map(c -> c.getFirst().getContent().getParts().getFirst().getText())
                .orElse("Không có phản hồi");
    }
}