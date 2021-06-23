package com.learning.transformers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import com.learning.messages.FeedBackCountRatingResult;
import com.learning.messages.FeedBackStoreValue;
import com.learning.messages.ItemFeedBack;
import com.learning.messages.ItemFeedBackResult;

public class FeedBackAverageTransformer implements ValueTransformer<ItemFeedBack, ItemFeedBackResult>{

	private ProcessorContext context;
	private final String storeName;
	private KeyValueStore<String, FeedBackStoreValue> feedbackStore;
	public FeedBackAverageTransformer(String storeName) {
		this.storeName=storeName;
	}
	@Override
	public void init(ProcessorContext context) {
		this.context=context;
		//also intiatlize the data store
		//like redis name of store is passed and each store contains multiple key value parirs
		//each entry's key name is itemName and value is storageObject
		this.feedbackStore = (KeyValueStore<String, FeedBackStoreValue>) this.context
				.getStateStore(storeName);
	}

	@Override
	public ItemFeedBackResult transform(ItemFeedBack value) {
		//fetch store value based on storeName
		//we already of store based on storeName
		//that is key value pair , key is item name and value is object containing
		String itemName = value.getItemName();
		FeedBackStoreValue currentStoreValue = Optional.ofNullable(feedbackStore.get(itemName))
					.orElse(new FeedBackStoreValue());
		
		long newTotalRating = value.getRating()+currentStoreValue.getRatingSum();
		long newRatingCount = currentStoreValue.getRatingCount()+1;
		currentStoreValue.setRatingSum(newTotalRating);
		currentStoreValue.setRatingCount(newRatingCount);
		
		feedbackStore.put(itemName, currentStoreValue);
		
		double averageRating = Math.round((double) newTotalRating / newRatingCount * 10d) / 10d;
		
		//lets query all the keys present in store
		KeyValueIterator<String, FeedBackStoreValue> allItems = feedbackStore.all();
		
		Map<String, FeedBackCountRatingResult> allItemsRating = new HashMap<>();
		while(allItems.hasNext()) {
			KeyValue<String, FeedBackStoreValue> currentEntry = allItems.next();
			long ratingCount = currentEntry.value.getRatingCount();
			long ratingSum = currentEntry.value.getRatingSum();
			double currentAverageRating = Math.round((double) ratingSum / ratingCount * 10d) / 10d;
			allItemsRating.put(currentEntry.key, new FeedBackCountRatingResult(ratingCount, currentAverageRating));
		}
		ItemFeedBackResult result = new ItemFeedBackResult(itemName, averageRating,allItemsRating);

		return result;
	}

	@Override
	public void close() {
		
	}

}
