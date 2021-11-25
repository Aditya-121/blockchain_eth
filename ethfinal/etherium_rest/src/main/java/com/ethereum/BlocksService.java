package com.ethereum;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BlocksService {

	private static int blockCount = 8;
	static List<Blocks> blockChain = new ArrayList<>();
	static List<Transactions> t2 = new ArrayList<>();
	
//	static {
//		blockChain.add(new Blocks(new Transactions(2,"Aditya","Ankit",1000,10),"0000"));
//		
//	}
	
	@Autowired
	private TransactioncheckRepo tcrepo;
	
	public void add(Blocks b) {
		// TODO Auto-generated method stub
		blockChain.add(b);
		
	}
	public int size() {
		// TODO Auto-generated method stub
		
		return blockChain.size() ;
	}
	public Blocks get(int i) {
		// TODO Auto-generated method stub
		List<Blocks>  blockChain= brepo.findAll();
		return blockChain.get(blockChain.size()-1);
	}
	
	public List<Transactions> getall()
	{
		
		return  trepo.findAll();
	}
	
	public void addTransac(Long id,Transactions t)
	{ 
		Transactions t1 = new Transactions();
		t1.block_id = id;
		t1.setSender(t.getSender());
		t1.setReceiver(t.getReceiver());
		t1.setTransaction_id(t.getTransaction_id());
		t1.setAmount(t.getAmount());
		t1.setGasfee(t.getGasfee());
		t2.add(t1);
		
	}
	@Autowired
	private TransactionsRepository trepo;
	
	public void save(Transactions t)
	{
		trepo.save(t);
		
	}
	public void save(Transactioncheck t1)
	{
		tcrepo.save(t1);
	}
	@Autowired
	private BlocksRepository brepo;
	 
	
	
	public List<Blocks> listAll() {
		return brepo.findAll();
	}
	
	
	public void save(Blocks block)
	{
		brepo.save(block);
		
	}
	public Blocks get(Long id)
	{
		return brepo.findById(id).get();
	}
	public Transactions getTransaction(Long id)
	{
		return trepo.findById(id).get();
	}
	
	
	
	public void delete(Long id)
	{
		brepo.deleteById(id);
	}

}
