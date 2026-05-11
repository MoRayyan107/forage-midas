package com.jpmc.midascore.entity;

import com.jpmc.midascore.foundation.Incentive;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "transaction_record")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id",nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id", nullable = false)
    private UserRecord receiver;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    public TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord receiver, Float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    // ------ SETTERS ---------
    public void setId(Long id) {
        this.transaction_Id = id;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public void setReceiver(UserRecord receiver) {
        this.receiver = receiver;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setIncentive(Float incentive) {
        this.incentive = incentive;
    }

    // -------------- GETTERS-----------------------
    public Long getId() {
        return transaction_Id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getReceiver() {
        return receiver;
    }

    public float getAmount() {
        return amount;
    }
}
