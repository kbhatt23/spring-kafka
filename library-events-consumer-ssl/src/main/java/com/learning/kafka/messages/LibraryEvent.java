package com.learning.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibraryEvent {
	private String eventId;
	private Book book;
	private EventType eventType;
}
