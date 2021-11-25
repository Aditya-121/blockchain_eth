package com.ethereum;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;




@Entity
@Table(name = "blocks")
public class Blocks {

	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "id")
	private Long id=0L;

	public String getmerkle_root() {
	return merkle_root;
}

public void setRoothash(String merkle_root) {
	this.merkle_root = merkle_root;
}
	@Column(name="prevhash")
	private String prevhash="00000";
	
	public String getPrevhash() {
		return prevhash;
	}

	public void setPrevhash(String prevhash) {
		this.prevhash = prevhash;
	}
	@Column(name="block_hash")
	private String block_hash;
	
	@Column(name="merkle_root")
	private String merkle_root;
	
	@Column(name="version")
	private double version;
	
	@Column(name="instant")
	@CreationTimestamp
	private Timestamp instant;
	
	

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public Timestamp getInstant() {
		return instant;
	}

	public void setInstant(Timestamp instant) {
		this.instant = instant;
	}
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "transaction_id")
	private List<Transactions> transactions =new ArrayList<Transactions>();
		
		public Blocks() {

		}

	public Blocks(List<Transactions> transactions,String hash) {
		
		this.version=3.0;
		this.prevhash=hash;
		this.transactions=transactions;
		//System.out.print("hello");
		this.block_hash = this.calculateHash();
	}
	
	
	public Long getId() {
		return id;
	}

	

	public List<Transactions> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transactions> t)
	{
		this.transactions = t;
	}
	

	
	
	public String getcurrhash() {
		return block_hash;
	}
	public void setHash()
	{
		this.block_hash= this.calculateHash();
	}

	
	private String calculateHash() {
		String message = Long.toString(id)+this.prevhash+this.merkle_root;
		MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA-256");
				byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
				this.block_hash = toHexString(hash);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return this.block_hash;
		}
		private String toHexString(byte[] hash)
		{
			BigInteger number = new BigInteger(1, hash);
			  
	        StringBuilder hexString = new StringBuilder(number.toString(16)); 
	        while (hexString.length() < 32) 
	        { 
	            hexString.insert(0, '0'); 
	        } 
	  
	        return hexString.toString(); 
		}

	

		

}
