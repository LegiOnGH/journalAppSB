package com.legion.journal.cache;

import com.legion.journal.entity.ConfigJournalEntry;
import com.legion.journal.repository.ConfigJournalRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalRepo configJournalRepo;

    public Map<String, String> appCache;

    @PostConstruct
    public void init(){
        appCache = new HashMap<>();
        List<ConfigJournalEntry> all = configJournalRepo.findAll();
        for(ConfigJournalEntry configJournalEntry : all){
            appCache.put(configJournalEntry.getKey(), configJournalEntry.getValue());
        }
    }
}
