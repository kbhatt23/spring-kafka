package com.learning.processors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.OrderFeedback;

//@Configuration
public class GoodFeedbackCountProcessorAllTogether {
	private static final String INPUT_TOPIC = "order-feedback";
	private static final String OUTPUT_TOPIC = "good-feedback";
	private static final String OUTPUT_BAD_TOPIC = "bad-feedback";
	
	private static final String OUTPUT_BAD_TOPIC_COUNT = "bad-feedback-count";
	
	private static final String OUTPUT_TOPIC_COUNT = "good-feedback-count";
	private static final Set<String> GOOD_WORDS ;
	private static final Set<String> BAD_WORDS ;
	
	static {
		GOOD_WORDS = new HashSet<>();
		GOOD_WORDS.add("happy");
		GOOD_WORDS.add("great");
		GOOD_WORDS.add("thank");
		GOOD_WORDS.add("class");
		
		BAD_WORDS = new HashSet<>();
		BAD_WORDS.add("bad");
		BAD_WORDS.add("shit");
	}
	
	@Bean
	public KStream<String, String> findGoodFeedbacks(StreamsBuilder builder){
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<OrderFeedback> jsonSerde = new JsonSerde<>(OrderFeedback.class);
		Serde<Long> longSerde = Serdes.Long();
		
		KStream<String, OrderFeedback> allFeedbackStream = builder.stream(INPUT_TOPIC , Consumed.with(stringSerde, jsonSerde));
		allFeedbackStream.print(Printed.<String, OrderFeedback>toSysOut().withLabel("findGoodFeedbacks: Original Stream"));
		
		//lets use branching
		//first get all the wors
		//use filter to sperate and branch good and bad words
		//get indepdnet streams
		
	KStream<String, String> unieuqWordsStream=	allFeedbackStream.flatMap((key,feedback) ->
			Arrays.asList(feedback.getMessage().trim()
						.split(" ")
						).stream()
					   .distinct()
					   .map(word -> KeyValue.pair(word,word))
					   .collect(Collectors.toList())
				);
		
	KStream<String, String>[] splittedStreams = unieuqWordsStream.branch((key,word) -> GOOD_WORDS.contains(word),
			(key,word) -> BAD_WORDS.contains(word));
	
	KStream<String, String> goodWordsStream = splittedStreams[0];
	KStream<String, String> badWordsStream = splittedStreams[1];
		
	//to is terminal and hence we can use though to contiue pipeline and save code
	//goodWordsStream.to(OUTPUT_TOPIC);
	goodWordsStream.through(OUTPUT_TOPIC)
	.groupByKey().count().toStream()
	.mapValues(String::valueOf)
	.to(OUTPUT_TOPIC_COUNT,Produced.with(stringSerde, stringSerde))
	;
				
		
		
		//bad word count
	badWordsStream.through(OUTPUT_BAD_TOPIC)
	.groupByKey().count().toStream().mapValues(String::valueOf)
	.to(OUTPUT_BAD_TOPIC_COUNT, Produced.with(stringSerde, stringSerde))
	;
	
		
		goodWordsStream.print(Printed.<String, String>toSysOut().withLabel("findGoodFeedbacks: good words Stream"));
		badWordsStream.print(Printed.<String, String>toSysOut().withLabel("findGoodFeedbacks: bad words Stream"));
		return goodWordsStream;
	}
	
	private List<String> findValidGoodWords(OrderFeedback feedback){
		String[] words = feedback.getMessage().trim()
						.split(" ")
						;
	return	Arrays.stream(words)
			  .filter(GOOD_WORDS::contains)
			  .distinct()
			  .collect(Collectors.toList());
	}
	
	//with key and value
	private List<KeyValue<String, String>> findValidGoodWords(String orderNumber,OrderFeedback feedback){
		String[] words = feedback.getMessage().trim()
						.split(" ")
						;
	return	Arrays.stream(words)
			  .map(String::toLowerCase)
			  .filter(GOOD_WORDS::contains)
			  .distinct()
			  .map(word -> KeyValue.pair(orderNumber, word))
			  .collect(Collectors.toList());
	}
	
	//with key and value
		private List<KeyValue<String, String>> findValidBadWords(String orderNumber,OrderFeedback feedback){
			String[] words = feedback.getMessage().trim()
							.split(" ")
							;
		return	Arrays.stream(words)
				  .map(String::toLowerCase)
				  .filter(BAD_WORDS::contains)
				  .distinct()
				  .map(word -> KeyValue.pair(orderNumber, word))
				  .collect(Collectors.toList());
		}
}
