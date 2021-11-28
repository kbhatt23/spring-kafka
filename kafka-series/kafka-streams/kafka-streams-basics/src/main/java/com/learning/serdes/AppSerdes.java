package com.learning.serdes;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.learning.entities.BankTransaction;

public class AppSerdes extends Serdes {

    static public final class BankTransactionSerde extends WrapperSerde<BankTransaction> {
        public BankTransactionSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    static public Serde<BankTransaction> bankTransaction() {
    	BankTransactionSerde serde =  new BankTransactionSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, BankTransaction.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

}
