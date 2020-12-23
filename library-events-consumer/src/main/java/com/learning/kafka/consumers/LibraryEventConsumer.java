package com.learning.kafka.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.learning.kafka.config.LibraryEventsConstants;
import com.learning.kafka.messages.LibraryEvent;
import com.learning.kafka.services.LibraryEventService;

@Service
public class LibraryEventConsumer {
	
	@Autowired
	private LibraryEventService libraryEventService;

	//since we have 3 partitions lets create 2 different consumer threads with same group id
	@KafkaListener(topics = LibraryEventsConstants.LIBRARY_TOPIC_NAME, concurrency = "3")
	public void recieveLibraryEvent(LibraryEvent libraryEvent) {
		libraryEventService.manageLibraryEvent(libraryEvent);
	}
}
