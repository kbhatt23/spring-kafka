package com.learning.kafka.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.kafka.config.LibraryEventsConstants;
import com.learning.kafka.messages.Book;
import com.learning.kafka.messages.LibraryEvent;

@RestController
@RequestMapping("/books")
public class BookEventController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private int counter;
	
	@Autowired
	private KafkaTemplate<String, LibraryEvent> kafkaTemplate; 
	
	

	@PostMapping
	public ResponseEntity<LibraryEvent> publishBookEvent(@RequestBody Book book) {

		if (!StringUtils.isEmpty(book.getBookId())) {
			throw new RuntimeException("Book can not have ID while pulishing event, Passed value " + book);
		}
		++counter;
		Book updatedBook = new Book("book-" + counter, book.getBookName(), book.getBookAuthor());
		LibraryEvent event = new LibraryEvent("event-" + counter, updatedBook);
		logger.info("publishBookEvent: Publishing library event for " + event);
		
		ListenableFuture<SendResult<String, LibraryEvent>> future = kafkaTemplate.send(LibraryEventsConstants.LIBRARY_TOPIC_NAME, event.getEventId(), event);
		
		future.addCallback(new ListenableFutureCallback<SendResult<String, LibraryEvent>>() {

			@Override
			public void onSuccess(SendResult<String, LibraryEvent> result) {
				logger.info("publishBookEvent: Library event {} succesfully sent to Kafka Topic {}",event,result.getRecordMetadata().topic()+"-"+result.getRecordMetadata().partition());
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("publishBookEvent: Library event {} failed for Kafka Topic {} with exception {}",event,LibraryEventsConstants.LIBRARY_TOPIC_NAME,ex.getMessage());
			}
		});
		
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
		LibraryEvent event = new LibraryEvent("event-" + counter, book);
		logger.info("updateBookEvent: Updating library event for " + event);
		return new ResponseEntity<>(event, HttpStatus.OK);
	}
}
