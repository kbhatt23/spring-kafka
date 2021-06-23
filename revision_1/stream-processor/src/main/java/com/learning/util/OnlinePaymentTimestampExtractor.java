package com.learning.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import com.learning.messages.OnlinePaymentMessage;

public class OnlinePaymentTimestampExtractor implements TimestampExtractor {

	@Override
	public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
		OnlinePaymentMessage onlinePaymentMessage = (com.learning.messages.OnlinePaymentMessage) record.value();

		return onlinePaymentMessage != null
				? LocalDateTimeUtil.toEpochTimestamp(onlinePaymentMessage.getCreationDate())
				: record.timestamp();
	}

}
