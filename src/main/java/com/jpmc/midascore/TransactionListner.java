package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.math.BigDecimal;

@Service
public class TransactionListner {

    private final Logger logger = LoggerFactory.getLogger(TransactionListner.class);


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;




    @KafkaListener(topics = "${general.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenTransactions(Transaction transaction) {
        logger.info("Consuming transaction: " + transaction.toString());
        transactionService.processTransaction(transaction);
    }
}
