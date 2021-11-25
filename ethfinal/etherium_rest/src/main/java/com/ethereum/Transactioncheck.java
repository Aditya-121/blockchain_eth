package com.ethereum;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name= "transactioncheck")
public class Transactioncheck {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "sender")
	private String sender;
	
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name = "gasfee")
	private double gasfee;
	
	@JsonFormat(pattern="MM/dd/yyyy")
	@Column(name="instant")
	private Timestamp instant;
	
	@Column(name="block_id")
	public long block_id;
	
	@Column(name ="transaction_hash")
	private String transaction_hash;
public String getTransaction_hash() {
		return transaction_hash;
	}

	public void setTransaction_hash(String transaction_hash) {
		this.transaction_hash = transaction_hash;
	}

	//	
	public Transactioncheck() {
	}
	
	public Transactioncheck(String sender, String receiver, double amount, double gasfee) {
		

		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.gasfee = gasfee;
		this.instant = Timestamp.from(Instant.now());
		
	}
	
	
	
	public Long getTransaction_id() {
		return id;
	}
	public void setTransaction_id(Long long1) {
		this.id = long1;
	}
	public String getSender() {
		return sender;
	}
//	public int getBlock_id() {
//		return block_id;
//	}
//
//	public void setBlock_id(int id2) {
//		this.block_id = id2;
//	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getGasfee() {
		return gasfee;
	}
	public void setGasfee(double gasfee) {
		this.gasfee = gasfee;
	}
	public void setInstant()
	{
		instant =  Timestamp.from(Instant.now());
	}
	public Timestamp getInstant() {
		return instant;
	}

	

}
