package com.pluralsight.books.repository;

import com.pluralsight.books.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
