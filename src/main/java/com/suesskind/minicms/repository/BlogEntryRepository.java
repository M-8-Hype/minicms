package com.suesskind.minicms.repository;

import com.suesskind.minicms.model.BlogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlogEntryRepository extends JpaRepository<BlogEntry, UUID> {
    List<BlogEntry> findDistinctByCategoriesNameIn(List<String> categoryNames);
}
