package com.microsservicos.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microsservicos.productapi.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
