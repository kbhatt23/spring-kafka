package com.learning.kafka.config;

public class LibraryEventsConstants {

	public static final String LIBRARY_TOPIC_NAME = "library-topic";
	public static final int LIBRARY_TOPIC_PARTITIONS = 3;
	public static final short LIBRARY_TOPIC_REPLICAS = 3;
	public static final String SURPRISE_BONUS_HEADER = "surpriseBonus";
	//will be sharing the same topic with 2 partition
	public static final String PROMOTION_DISCOUNT_TOPIC = "promotion-discount-topic";
	public static final String PROMOTION_KEY = "promotion-key";
	public static final String DISCOUNT_KEY = "discount-key";
	public static final String LIBRARY_ACKNOWLEDGE_TOPIC = "library-acknowledge-topic";
}
