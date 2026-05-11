package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.math.BigDecimal;

@Service
public class TransactionListner {

    private final Logger logger = LoggerFactory.getLogger(TransactionListner.class);


    @KafkaListener(topics = "${general.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenTransactions(Transaction transaction) {
        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
        System.out.println("Received transaction: " + transaction);
    }
}
