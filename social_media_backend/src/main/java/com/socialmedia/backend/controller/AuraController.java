package com.socialmedia.backend.controller;

import com.socialmedia.backend.exception.GlobalExceptionHandler;
import com.socialmedia.backend.request.AuraRequest;
import com.socialmedia.backend.response.AuraResponse;
import com.socialmedia.backend.service.AuraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aura")
public class AuraController {
    @Autowired
    private AuraService auraService;

    @PostMapping("/chat")
    public ResponseEntity<AuraResponse> chat(@RequestBody AuraRequest chatRequest) throws GlobalExceptionHandler {
        AuraResponse response = auraService.processChat(chatRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/explain")
    public ResponseEntity<String> explain(@RequestParam String topic) {
        String explanation = auraService.getShortExplanation(topic);
        return ResponseEntity.ok(explanation);
    }
}
