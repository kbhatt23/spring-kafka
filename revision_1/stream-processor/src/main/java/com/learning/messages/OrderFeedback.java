package com.learning.messages;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class OrderFeedback {

	private String orderNumber;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime feedbackTime;
	
	private String message;
	
	private Rating rating;

	public OrderFeedback() {
		super();
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDateTime getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(LocalDateTime feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "OrderFeedback [orderNumber=" + orderNumber + ", feedbackTime=" + feedbackTime + ", message=" + message
				+ ", rating=" + rating + "]";
	}
	
	
}
