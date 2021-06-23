package com.learning.processors;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.learning.messages.WebColorVoteMessage;
import com.learning.messages.WebDesignVoteMessage;
import com.learning.messages.WebLayoutVoteMessage;
import com.learning.util.WebColorVoteTimestampExtractor;
import com.learning.util.WebLayoutVoteTimestampExtractor;

//@Configuration
public class VoteCalculationProcessor {

	
	//@Bean
	public KStream<String, WebColorVoteMessage> handleColorVote(StreamsBuilder builder){
		System.out.println("starting config for vote color with builder "+builder);
		Serde<String> stringSerde = Serdes.String();
		JsonSerde<WebColorVoteMessage> colorVoteSerde = new JsonSerde<>(WebColorVoteMessage.class);
		JsonSerde<WebLayoutVoteMessage> layoutVoteSerde = new JsonSerde<>(WebLayoutVoteMessage.class);
		JsonSerde<WebDesignVoteMessage> mergedJsonSerde = new JsonSerde<>(WebDesignVoteMessage.class);
		
	
		WebColorVoteTimestampExtractor colorTimeExtractor = new WebColorVoteTimestampExtractor();
		KStream<String, WebColorVoteMessage> streamColorVote = builder.stream("colorvote",Consumed.with(stringSerde, colorVoteSerde, colorTimeExtractor,null));
		
		streamColorVote.print(Printed.<String, WebColorVoteMessage>toSysOut().withLabel("handleColorVote: Original color vote Stream"));
	
		WebLayoutVoteTimestampExtractor layoutTimeExtractor = new WebLayoutVoteTimestampExtractor();
		KStream<String, WebLayoutVoteMessage> streamLayoutVote = builder.stream("layoutvote",Consumed.with(stringSerde, layoutVoteSerde, layoutTimeExtractor,null));
		
		streamLayoutVote.print(Printed.<String, WebLayoutVoteMessage>toSysOut().withLabel("handleColorVote: Original layout vote Stream"));
		
		//we are pushing to table topic directly as key is already set as username for both the kind of votes
		//no need to update the key as key username is unique and is availbe for updation anytime
		
		streamColorVote.to("colorvote-table",Produced.with(stringSerde, colorVoteSerde));
		streamLayoutVote.to("layoutvote-table",Produced.with(stringSerde, layoutVoteSerde));
		
		KTable<String, WebColorVoteMessage> colorTable = builder.table("colorvote-table",Consumed.with(stringSerde, colorVoteSerde,colorTimeExtractor,null))
			;
		
		KTable<String, WebLayoutVoteMessage> voteTable = builder.table("layoutvote-table",Consumed.with(stringSerde, layoutVoteSerde, layoutTimeExtractor,null))
				;
		
		
		KTable<String, WebDesignVoteMessage> mergedTable= colorTable.join(voteTable, this :: mergeLayoutAndColorVotes, 
				Materialized.with(stringSerde, mergedJsonSerde));
		
		mergedTable.toStream()
					.through("vote-final",Produced.with(stringSerde, mergedJsonSerde))
					.print(Printed.<String, WebDesignVoteMessage>toSysOut().withLabel("handleColorVote: final Stream"));
					;
		
		return streamColorVote;
	}

	private  WebDesignVoteMessage mergeLayoutAndColorVotes(WebColorVoteMessage webcolorvotemessage, WebLayoutVoteMessage webLayoutVoteMessage) {
		WebDesignVoteMessage webDesignVoteMessage = new WebDesignVoteMessage();
		webDesignVoteMessage.setColor(webcolorvotemessage.getColor());
		webDesignVoteMessage.setLayout(webLayoutVoteMessage.getLayout());
		return webDesignVoteMessage;
	}
}
