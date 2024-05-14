package com.learning.kafka.emailnotificationservice.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="processed-events")
public class ProcessedEventEntity implements Serializable {

	private static final long serialVersionUID = 3687553269742697084L;
	
	@Id
	private String messageId;

	@Column(unique = true ,nullable = false)
	private String messageKey;

	public ProcessedEventEntity() {
	}

	public ProcessedEventEntity(String messageId, String messageKey) {
		this.messageId=messageId;
		this.messageKey=messageKey;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}