package com.legion.journal.scheduler;

import com.legion.journal.cache.AppCache;
import com.legion.journal.entity.JournalEntry;
import com.legion.journal.entity.User;
import com.legion.journal.enums.Sentiment;
import com.legion.journal.repository.UserRepoImp;
import com.legion.journal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImp userRepoImp;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 ? * SUN *")
    public void fetchUserSendSaMail(){
        List<User> users = userRepoImp.getUsersForSA();
        for(User user: users){
            List<JournalEntry> journalEntry = user.getJournalEntry();
            List<Sentiment> sentiments = journalEntry.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(JournalEntry::getSentiment).toList();
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment: sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment,0)+1);;
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry: sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount= entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if(mostFrequentSentiment!=null) {
                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * * *")
    public void clearAppCache(){
        appCache.init();
    }
}

