package com.legion.journal.service;


import com.legion.journal.entity.JournalEntry;
import com.legion.journal.entity.User;
import com.legion.journal.enums.Sentiment;
import com.legion.journal.repository.JournalEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName, Sentiment sentiment){
        User user = userService.findByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        journalEntry.setSentiment(sentiment);
        JournalEntry saved = journalEntryRepo.save((journalEntry));
        user.getJournalEntry().add(saved);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save((journalEntry));
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntry().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepo.deleteById(id);
            }
            log.info("haha you noob");
            log.warn("haha you noob");
            log.error("haha you noob");
            log.debug("haha you noob"); //won't come unless enabled
            log.trace("haha you noob"); //won't come unless enabled


        }catch(Exception e){
            throw new RuntimeException("Error occurred while deleting journal entry : ", e);
        }
        return removed;
    }
}
