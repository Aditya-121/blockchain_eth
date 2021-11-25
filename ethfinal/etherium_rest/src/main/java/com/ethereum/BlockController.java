package com.ethereum;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javassist.bytecode.analysis.ControlFlow.Block;






@RestController
public class BlockController {
	
	@Autowired
	private BlocksService service;
	@Autowired
	private BlocksRepository brepo;
	
	@Autowired 
	private TransactionsRepository trepo;
	
	@Autowired
	private TransactioncheckRepo tcrepo;
	
	@Autowired
	private MemPoolRepo mrepo;

	Merkle m=new Merkle();
	int id=1;
	
	
	@GetMapping("/blocks")
	public List<Blocks> list() {
		List<Blocks> b = service.listAll();
		return b;
	}
	/*@GetMapping(path="/block-data")
	public Blocks check()
	{
		Transactions firstTransaction=new Transactions("Aditya" ,"Nikhil",1000,10);
		Blocks addBlock= new Blocks(firstTransaction,"0000") ;
		service.save(addBlock);
		return addBlock;
	}*/
	@GetMapping("/blocks/{id}")
	public ResponseEntity<Blocks> get(@PathVariable Long id) {
		try {
			Blocks blocks = service.get(id);
			System.out.println(blocks.getTransactions().size());
			return new ResponseEntity<Blocks>(blocks, HttpStatus.OK);
		} catch(NoSuchElementException e)
		{
			return new ResponseEntity<Blocks>(HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping("blocks/addTransaction")
	public String addTransaction(@RequestBody Transactions transaction)
	{
//		Transactioncheck tr1 = new Transactioncheck(transaction.getSender(),transaction.getReceiver(),transaction.getAmount(),transaction.getGasfee());
		List<MemPool> mr = mrepo.findAll();
		if(mr.size()<20)
		{
			MemPool  m = new MemPool(transaction.getSender(),transaction.getReceiver(),transaction.getAmount());
			mr.add(m);
			mrepo.save(m);
			
		}
		else
		{
			List<Blocks>  br  = brepo.findAll();
			List<MemPool> m1 = mrepo.findAll();
			List<Transactions> tr = new ArrayList<Transactions>();
			for(MemPool me : m1)
			{
				Transactions  t = new Transactions(me.getSender(),me.getReceiver(),me.getAmount(),1.0);
				String Message = t.getSender()+t.getReceiver()+Double.toString(t.getAmount())+Double.toString(t.getGasfee());
				t.setTransaction_hash(Merkle.calculateHash(Message));
				
				t.block_id=(long) (br.size()+1);
				tr.add(t);
				
			}
			
			if(br.size()==0)
			{
				Blocks b = new Blocks(tr,"0000");
				b.setRoothash(m.createMerkel(1, tr));
				b.setHash();
				brepo.save(b);
				mrepo.deleteAll();
				
			}
			else
			{
				Blocks b = new Blocks(tr,br.get(br.size()-1).getcurrhash());
				b.setRoothash(m.createMerkel(br.size()+1, tr));
				b.setHash();
				brepo.save(b);
				mrepo.deleteAll();
			}
			MemPool  m = new MemPool(transaction.getSender(),transaction.getReceiver(),transaction.getAmount());
			mrepo.save(m);
		}
		
		
		return "Transaction is added successfully";
		
		
		
	}
//		double total=10000.0,sum=0;
//		List<Blocks> blo= brepo.findAll();
//		Blocks  block=null;
//		if(blo.size()==0)
//		{
//			 block=null;
//			 
//		}
//		else
//		{
//			block = blo.get(blo.size()-1);
//		}
//		List<Blocks> br = brepo.findAll();
//		if(block==null)
//		{
//			List<Transactions> tr=new ArrayList<Transactions>();
//			if(transaction.getGasfee()<=total)
//			{
//				transaction.block_id=1L;
//				tr1.block_id=1L;
//				String message  = transaction.getSender()+transaction.getReceiver()+transaction.getAmount()+transaction.getGasfee();
//				transaction.setTransaction_hash(Merkle.calculateHash(message));
//				tr1.setTransaction_hash(Merkle.calculateHash(message));
//				tr.add(transaction);
//				Blocks b=new Blocks(tr,"0000");
//				b.setRoothash(m.createMerkel(1, tr));
//				b.setHash();
//				brepo.save(b);
//				
//			}
//			else
//			{
//				return "gasfee of transaction exceeded";
//			}
//		}
//		else
//		{
//			System.out.print("blockd"+br.get(br.size()-1).getId());
//			List<Transactions> tr=new ArrayList<Transactions>();
////			List<Transactions> tr5 = new ArrayList<Transactions>();
//			List<Transactions> savedTransacs=trepo.findAll();
//			for(Transactions t:savedTransacs)
//			{
//				if(t.block_id==br.get(br.size()-1).getId())
//				{
//					sum+=t.getGasfee();
//				}
//			}
//			
//			sum+=transaction.getGasfee();
//			System.out.print(sum);
//			if(sum<=total)
//			{
//				
//				for(Transactions t:savedTransacs)
//				{
//					if(t.block_id==br.get(br.size()-1).getId())
//					{
//						tr.add(t);
//					}
//				}
////				System.out.print("dskjfk");
//				transaction.block_id=br.get(br.size()-1).getId();
//				tr1.block_id=br.get(br.size()-1).getId();
//				String message  = transaction.getSender()+transaction.getReceiver()+transaction.getAmount()+transaction.getGasfee();
//				transaction.setTransaction_hash(Merkle.calculateHash(message));
//				tr1.setTransaction_hash(Merkle.calculateHash(message));
//				tr.add(transaction);
//				block.setTransactions(tr);
//				System.out.println("ubdivndodsmvsdomvomdspnv " + block.getTransactions().size()+"   IHNIN "+block.getId());
//				
//				
//				block.setRoothash(m.createMerkel(id, tr));
//				block.setHash();
//				
//				brepo.save(block);
//			}
//			else {
//				
//				Blocks b = br.get(br.size()-1);
//				Blocks b1=new Blocks(new ArrayList<Transactions>(),b.getcurrhash());
//				transaction.block_id=br.get(br.size()-1).getId()+1;
//				tr1.block_id=br.get(br.size()-1).getId()+1;
//				String message  = transaction.getSender()+transaction.getReceiver()+transaction.getAmount()+transaction.getGasfee();
//				transaction.setTransaction_hash(Merkle.calculateHash(message));
//				tr1.setTransaction_hash(Merkle.calculateHash(message));
//				tr.add(transaction);
//				
//				b1.setTransactions(tr);
//				b1.setRoothash(m.createMerkel(id, tr));
//				b1.setHash();
//				brepo.save(b1);
//			}
//			//trepo.save(transaction);
//		}
//		service.save(tr1);
//		
//		return "Transaction is added successfully";
//	}
	
	@GetMapping("blocks/{id}/transactions")
	public List<Transactions> get(@PathVariable long id)
	{
//		try
//		{
//			Transactions t1 = service.getTransaction((int) id);
////			System.out.println(t1.block_id);
//			return new ResponseEntity<Transactions>((Transactions) t1,HttpStatus.OK);
//			
//		}
//		catch(NoSuchElementException e)
//		{
//			return new ResponseEntity<Transactions>(HttpStatus.NOT_FOUND);
//			
//		}
		return service.getall();
	}
	

	@GetMapping("/chainverification")
	public String chainverifcation()
	{
		List<Blocks> br = brepo.findAll();
		int j=0;
		for(int i=1;i<br.size();i++)
		{
			if(br.get(i).getPrevhash()!=br.get(i-1).getcurrhash())
			{
				j=i;
				break;
			}
		}
		if(j==0)
		{
			return "Block chain is fine";
		}
		return "Block " +Integer.toString(j) + "invalid";
	}
	
	
	@GetMapping("/verify/block/{id}")
	public String verification(@PathVariable int id)
	{
		List<Transactions> t5 = trepo.findAll();
		List<Transactions> t7 = new ArrayList<Transactions>();
		for(int i=0;i<t5.size();i++)
		{
			if(t5.get(i).block_id==id)
			{
				t7.add(t5.get(i));
				
			}
		}
		String mess = m.createMerkel(id, t7);
		Blocks  b = service.get(id);
//		for(int i=0;i<t7.size();i++)
//		{
//			System.out.println(b.getTransactions().get(i).getSender()+"      "+t7.get(i).getSender());
//			System.out.println(b.getTransactions().get(i).getReceiver()+"      "+t7.get(i).getReceiver());
//			System.out.println(b.getTransactions().get(i).getAmount()+"      "+t7.get(i).getAmount());
//			System.out.println(b.getTransactions().get(i).getGasfee()+"      "+t7.get(i).getGasfee());
//			
//		}
//		System.out.println(b.getmerkle_root()+"      "+mess);
		if(mess.equals(b.getmerkle_root()))
		{
			return "block is fine";
		}
		return "Block is invalid";
//		r =m.checkVerification(k, t7,t8);
//		if(r==1)
//		{
//			return "BlockChain is fine";
//		}
//		return Long.toString(r);
	}
	@GetMapping("transaction/{id}")
	public Transactions transaction_view(@PathVariable Long id)
	{
		return service.getTransaction(id);
	}
	@GetMapping("verify/transaction/{id}")
	public String transaction_verification(@PathVariable Long id)
	{
		Transactions  t = service.getTransaction(id);
		String message = t.getSender()+t.getReceiver()+Double.toString(t.getAmount())+Double.toString(t.getGasfee());
		String calcuateMessage = Merkle.calculateHash(message);
		if(t.getTransaction_hash().equals(calcuateMessage))
		{
			return "transaction is fine";
		}
		return "transaction is invalid";
		
	}
	@PostMapping("/blocks")
	public void add(@RequestBody Transactions transactions) {
		Blocks b= new Blocks(null,service.get(service.size()-1).getcurrhash());
		service.add(b);
		service.save(b);
	}
	
	@PutMapping("/blocks/{id}")
	public ResponseEntity<?> update(@RequestBody Blocks blocks, @PathVariable Long id)
			{
		try {
			Blocks existBlock =service.get(id);
			service.save(blocks);
			return new ResponseEntity<Blocks>(blocks,HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Blocks>(HttpStatus.NOT_FOUND);
		}
		
			}
	
	@DeleteMapping("/blocks/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

}
