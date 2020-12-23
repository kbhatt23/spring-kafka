package com.learning.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import com.learning.kafka.messages.LibraryEvent;
import com.learning.kafka.services.LibraryEventService;
//disabling it
//@Service
public class LibraryManualOffSetCommitEventConsumer implements AcknowledgingMessageListener<String, LibraryEvent>{
	
	@Autowired
	private LibraryEventService libraryEventService;

	//since we have 3 partitions lets create 2 different consumer threads with same group id
	//@KafkaListener(topics = LibraryEventsConstants.LIBRARY_TOPIC_NAME, concurrency = "3")
//	public void recieveLibraryEvent(LibraryEvent libraryEvent) {
//		libraryEventService.manageLibraryEvent(libraryEvent);
//	}

	@Override
	//@KafkaListener(topicPattern = LibraryEventsConstants.LIBRARY_TOPIC_NAME,containerFactory = "manualOffSetCommitFactory")
	public void onMessage(ConsumerRecord<String, LibraryEvent> data, Acknowledgment acknowledgment) {

		libraryEventService.manageLibraryEvent(data.value());
		//until we call this method in case of manual acknowledgement it wont update the offset and if we restart consumer it will again try to consume the same message
		//however rety is not configrued and hence wont retry
		acknowledgment.acknowledge();
	}
}
