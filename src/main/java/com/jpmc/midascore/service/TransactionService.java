package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        // get the Users
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord reciver = userRepository.findById(transaction.getRecipientId());

        // ceck if the user id is null or not
        if (sender == null || reciver == null){
            logger.error("User not found for transaction: " + transaction.toString());
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.error("Cannot process transaction because sender balance is less than transaction amount");
            return;
        }

        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction, Incentive.class
        );

        float incentiveAmount = (incentive != null) ? 0.0f : incentive.getAmount();

        // do the cahnges like Sender will be deducted and Receiver gets added
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        reciver.setBalance(reciver.getBalance() + transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(reciver);

        // create a record
        TransactionRecord transactionRec = new TransactionRecord();
        transactionRec.setSender(sender);
        transactionRec.setReceiver(reciver);
        transactionRec.setAmount(transaction.getAmount());

        // save to repo
        transactionRepository.save(transactionRec);
//        logger.info("Transaction record processed successfully from Sender: "+sender.getName()+" to Receiver: "+reciver.getName()+" for amount: "+transaction.getAmount());
//        logger.info("Sender: "+sender.getName()+" Balance: "+sender.getBalance());
//        logger.info("Reciever: "+reciver.getName()+" Balance: "+reciver.getBalance());
    }
}
