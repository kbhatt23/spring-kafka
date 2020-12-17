package com.learning.kafka;

public interface OrderServiceConstants {

	public static final String ORDER_TOPIC_NAME = "order-topic";
	public static final int ORDER_TOPIC_PARTITIONS = 2;
	public static final short ORDER_TOPIC_REPLICAS = 1;
	public static final String SURPRISE_BONUS_HEADER = "surpriseBonus";

	//will be sharing the same topic with 2 partition
	public static final String PROMOTION_DISCOUNT_TOPIC = "promotion-discount-topic";
	public static final String PROMOTION_KEY = "promotion-key";
	public static final String DISCOUNT_KEY = "discount-key";
	public static final String ORDER_ACKNOWLEDGE_TOPIC = "order-acknowledge-topic";
}
