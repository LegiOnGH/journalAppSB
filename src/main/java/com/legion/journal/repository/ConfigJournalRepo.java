package com.legion.journal.repository;

import com.legion.journal.entity.ConfigJournalEntry;
import com.legion.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigJournalRepo extends MongoRepository<ConfigJournalEntry, ObjectId> {
}
