package com.suesskind.minicms.repository;

import com.suesskind.minicms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> { }
