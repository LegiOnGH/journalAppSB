package com.legion.journal.service;

import com.legion.journal.model.SentimentData;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "sentiment", groupId = "sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    private void sendEmail(@NotNull SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(),"Sentiment Data for Previous Week ", sentimentData.getSentiment());
    }
}
