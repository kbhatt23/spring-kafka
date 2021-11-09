package com.learning.kafka_basics.producers;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.common.collect.Lists;
import com.learning.kafka_basics.config.KafkaConfig;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterProducer {

	public static void main(String[] args) {
		new TwitterProducer().run();
	}

	private void run()  {
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10);
		Client client = createClient(msgQueue);
		client.connect();
		KafkaProducer<String, String> createProducer = createProducer();
		
		while(!client.isDone()) {
			try {
				String tweetMessage = msgQueue.poll(5, TimeUnit.SECONDS);
				if(null == tweetMessage)
					continue;
				createProducer.send(new ProducerRecord<>("twitter-feed-topic", tweetMessage), new Callback() {
					
					@Override
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						if(exception != null) {
							System.err.println("TwitterProducer: Error occurred while sending twitter feed to kafka : "+exception.getMessage());
						}
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		createProducer.flush();
		createProducer.close();
	}

	private Client createClient(BlockingQueue<String> msgQueue ) {

		/** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */

		/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		List<String> terms = Lists.newArrayList("kafka", "redis");
		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1("cfmh7IatqWjzxOsT9Wy2WWzXZ", "d4rGSUFVZv4F9pzQGcG7vF1ZXsUVkZcrBm2CDX1Hhf9EWdv6Mk", "1458026502679789571-wTdquDfWa8TdFnDHKCq7lmyxRJg1Ap", "l5zUgdDdrnfyvEIkyRsZQZQ73uhiMj3dxrXxIRmlg3EAm");

		
		ClientBuilder builder = new ClientBuilder()
				  .name("kafka-elasticsearch-feed")                              // optional: mainly for the logs
				  .hosts(hosebirdHosts)
				  .authentication(hosebirdAuth)
				  .endpoint(hosebirdEndpoint)
				  .processor(new StringDelimitedProcessor(msgQueue));

				Client hosebirdClient = builder.build();
				
				return hosebirdClient;
	}
	
	private KafkaProducer<String, String> createProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVER);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.STRING_SERIALIZER);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.APPLICATION_ID);

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		return producer;
	
	}
}
