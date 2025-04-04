package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.Aura;
import com.socialmedia.backend.repository.AuraChatMessageRepository;
import com.socialmedia.backend.request.AuraRequest;
import com.socialmedia.backend.response.AuraResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuraService {
    @Autowired
    public GeminiService geminiService;

    @Autowired
    public AuraChatMessageRepository auraChatMessageRepository;

    /*public AuraService(GeminiService geminiService, AuraChatMessageRepository auraChatMessageRepository) {
        this.geminiService = geminiService;
        this.auraChatMessageRepository = auraChatMessageRepository;
    }*/

    public AuraResponse processChat(AuraRequest request) {
        String aiReply = geminiService.getChatCompletion(request.getContent());

        Aura auraMessage = new Aura();
        auraMessage.setSender(request.getSender());
        auraMessage.setContent(request.getContent());
        auraChatMessageRepository.save(auraMessage);

        // Tạo phản hồi AuraResponse
        AuraResponse response = new AuraResponse();
        response.setContent(aiReply);
        return response;
    }
    public String getShortExplanation(String topic) {
        String prompt = "Hãy giải thích ngắn gọn về " + topic + " trong 2-3 câu.";
        return geminiService.getChatCompletion(prompt);
    }
}
