package com.legion.journalApp.controller;

import com.legion.journalApp.entity.JournalEntry;
import com.legion.journalApp.entity.User;
import com.legion.journalApp.service.JournalEntryService;
import com.legion.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntry();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> byId = journalEntryService.findById(myId);
        return byId.map(journalEntry -> new ResponseEntity<>(journalEntry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName){
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry myEntry,
            @PathVariable String userName
    ){
        JournalEntry journalEntry = journalEntryService.findById(myId).orElse(null);
        if(journalEntry!=null){
            journalEntry.setTitle(myEntry.getTitle()!=null && !myEntry.getTitle().isEmpty() ?myEntry.getTitle(): journalEntry.getTitle());
            journalEntry.setContent(myEntry.getContent()!=null && !myEntry.getContent().isEmpty() ?myEntry.getContent(): journalEntry.getContent());
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
