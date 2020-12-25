package com.learning.kafka.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.kafka.messages.Book;
import com.learning.kafka.messages.EventType;
import com.learning.kafka.messages.LibraryEvent;
import com.learning.kafka.services.LibraryEventProducer;

@RestController
@RequestMapping("/books")
public class BookEventController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private int counter;
	
	@Autowired
	private LibraryEventProducer libraryEventProducer;
	

	@PostMapping
	public ResponseEntity<LibraryEvent> publishBookEvent(@RequestBody Book book) {

		if (!StringUtils.isEmpty(book.getBookId())) {
			throw new RuntimeException("Book can not have ID while pulishing event, Passed value " + book);
		}
		++counter;
		Book updatedBook = new Book("book-" + counter, book.getBookName(), book.getBookAuthor());
		LibraryEvent event = new LibraryEvent("event-" + counter, updatedBook, EventType.CREATE);
		logger.info("publishBookEvent: Publishing library event for " + event);
		
		//no error handing or retrying as of now , in case of error
		//method is async in nature
		libraryEventProducer.sendLibraryEvent(event);
		
		return new ResponseEntity<>(event, HttpStatus.CREATED);
	}

	@PutMapping("/{bookId}")
	public ResponseEntity<LibraryEvent> updateBookEvent(@RequestBody Book book,@PathVariable String bookId) {
		if (StringUtils.isEmpty(book.getBookId())) {
			throw new RuntimeException("Book can not have empty ID while updating event, Passed value " + book);
		}
		if (StringUtils.isEmpty(bookId)) {
			throw new RuntimeException("Book Id parameter can not be empty, Passed value " + bookId);
		}
		if(!bookId.equals(book.getBookId())) {
			throw new RuntimeException("Book Id parameter must be equal to book Id in JSON, passed values bookID parameter " + bookId+" , book JSON: "+book);
		}
		++counter;
		LibraryEvent event = new LibraryEvent("event-" + counter, book, EventType.UPDATE);
		logger.info("updateBookEvent: Updating library event for " + event);
		
		//no error handing or retrying as of now , in case of error
				//method is async in nature
		libraryEventProducer.sendLibraryEvent(event);
		
		return new ResponseEntity<>(event, HttpStatus.OK);
	}
}
