package com.learning.kafka.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;

import com.learning.kafka.entities.BookEntity;
import com.learning.kafka.messages.Book;
import com.learning.kafka.messages.EventType;
import com.learning.kafka.messages.LibraryEvent;
import com.learning.kafka.repositories.BooksRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryEventService {

	@Autowired
	private BooksRepository booksRepository;
	
	
	public void manageLibraryEvent(LibraryEvent libraryEvent) {
		
		log.info("manageLibraryEvent: Managing library event "+libraryEvent);
		//ignore event id
		//no need of vlaidation, this is already taken care in library event controller using @valid
		//create book entity first
		Book book = libraryEvent.getBook();
		BookEntity bookEntity = new BookEntity(book.getBookId(), book.getBookName(), book.getBookAuthor());
		
		//assumig that there was some web service call in consume and third party service is temporarily down
		//so it is common in microservice architechture for some service to have network issue, hence retrying makes sense
		if(libraryEvent.getBook().getBookId().equals("book-1")) {
			throw new RecoverableDataAccessException("Third party is down,kindly retry");
		}
		if(libraryEvent.getEventType().equals(EventType.CREATE)) {
			booksRepository.save(bookEntity);
		}else if(libraryEvent.getEventType().equals(EventType.UPDATE)){
			Optional<BookEntity> findById = booksRepository.findById(book.getBookId());
			findById.map(existing -> booksRepository.save(bookEntity))
			//throwing non recoverable exception: as if nook id is not present retrying will be time waste
					.orElseThrow(() -> new IllegalArgumentException("book do not exist with id "+book.getBookId()));
			;
			
			
		}
	}
}
