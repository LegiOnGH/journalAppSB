package com.legion.journal.controller;

import com.legion.journal.entity.JournalEntry;
import com.legion.journal.entity.User;
import com.legion.journal.enums.Sentiment;
import com.legion.journal.service.JournalEntryService;
import com.legion.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
@Tag(name="Journal APIs")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    @Operation(summary = "Get all Journal Entries of User")
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntry();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/{myId}")
    @Operation(summary = "Get Journal Entry by ID")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectId objectId = new ObjectId(myId);
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntry().stream().filter(x -> x.getId().equals(objectId)).toList();
        if (!collect.isEmpty()) {
            Optional<JournalEntry> byId = journalEntryService.findById(objectId);
            if(byId.isPresent()){
                return new ResponseEntity<>(byId.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete/{myId}")
    @Operation(summary = "Delete a Journal Entry")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String myId) {
        ObjectId objectId = new ObjectId(myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(objectId, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{myId}")
    @Operation(summary = "Update a Journal Entry")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable String myId,
            @RequestBody JournalEntry myEntry
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectId objectId = new ObjectId(myId);
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntry().stream().filter(x->x.getId().equals(objectId)).toList();
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(!myEntry.getTitle().isEmpty() ? myEntry.getTitle() : old.getTitle());
                old.setContent(myEntry.getContent() != null && !myEntry.getContent().isEmpty() ? myEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("createEntry")
    @Operation(summary = "Create a Journal Entry")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Sentiment sentiment = journalEntry.getSentiment();
            journalEntryService.saveEntry(journalEntry, userName, sentiment);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
