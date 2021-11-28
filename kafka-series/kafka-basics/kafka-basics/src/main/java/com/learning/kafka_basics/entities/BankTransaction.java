package com.learning.kafka_basics.entities;

public class BankTransaction {

	private String userName;

	private double amount;

	private TransactionType transactionType;

	public BankTransaction() {
		super();
	}

	public BankTransaction(String userName, double amount, TransactionType transactionType) {
		super();
		this.userName = userName;
		this.amount = amount;
		this.transactionType = transactionType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String toString() {
		return "BankTransaction [userName=" + userName + ", amount=" + amount + ", transactionType=" + transactionType
				+ "]";
	}

}
