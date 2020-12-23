package com.learning.kafka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.kafka.entities.BookEntity;

@Repository
public interface BooksRepository extends JpaRepository<BookEntity, String>{

}
