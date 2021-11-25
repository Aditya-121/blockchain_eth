package com.ethereum;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;





class Node
{
	private Node left;
	private Node right;
	private String Hash;
	
	Node(Node left,Node right,String Hash)
	{
		this.left=left;
		this.right = right;
		this.Hash=Hash;
		
	}
	
	public Node getLeft()
	{
		return left;
	}
	
	public void setLeft(Node left) {
		this.left=left;
	}
	public void setRight(Node right) {
		this.right=right;
	}
	public Node getRight()
	{
		return right;
	}
	public void setHash(String Hash)
	{
		this.Hash=Hash;
	}
	public String getHash()
	{
		return Hash;
	}
	
		
}


public class Merkle {

	
	@Autowired
	BlocksRepository brepo;
	@Autowired
	TransactionsRepository trepo;
	public  String createMerkel(int id,List<Transactions> tr)
	{
		List<String> messages = new ArrayList<String>();
		for (Transactions t : tr)
		{
			messages.add(t.getSender()+t.getReceiver()+Double.toString(t.getAmount())+Double.toString(t.getGasfee()));
		}
		
		List<Node> dataNodes = new ArrayList<Node>();
		for(String d: messages)
		{
			dataNodes.add(new Node(null,null,calculateHash(d)));
			
		}
		int nsize = dataNodes.size();
		int i=0;
		while(nsize>=(Math.pow(2,i)))
		{
			i+=1;
		}
		System.out.println(nsize+" nodes   "+Math.pow(2,i));
		int t = (int) Math.pow(2, i);
		for(int j=nsize;j<t;j++)
		{
			dataNodes.add(new Node(null,null,calculateHash("Null+Null")));
			
		}
		Node n = buildTree(dataNodes);
		return n.getHash();
	}
	
	private  Node buildTree(List<Node> children)
	{
		
		List<Node> parents = new ArrayList<Node>();
		List<Node> outp = new ArrayList<Node>();
		
		while(children.size()!=1)
		{
			int index =0,length=children.size();
			while(index<length-1)
			{
				Node leftChild = children.get(index);
				Node rightchild = children.get(index+1);
				
				outp.add(leftChild);
				outp.add(rightchild);
				String parentHash = calculateHash(leftChild.getHash()+rightchild.getHash());
				
				parents.add(new Node(leftChild,rightchild,parentHash));
				index+=2;
				
			}
			children = parents;
			parents = new ArrayList<Node>();
			
		}
		outp.add(children.get(0));
		
		return children.get(0);
		
	}
	private  List<Node> buildTree(List<Node> children,int choi)
	{
		List<Node> parents = new ArrayList<Node>();
		List<Node> outp = new ArrayList<Node>();
		
		while(children.size()!=1)
		{
			int index =0,length=children.size();
			while(index<length-1)
			{
				Node leftChild = children.get(index);
				Node rightchild = children.get(index+1);
				
				outp.add(leftChild);
				outp.add(rightchild);
				String parentHash = calculateHash(leftChild.getHash()+rightchild.getHash());
				
				parents.add(new Node(leftChild,rightchild,parentHash));
				index+=2;
				
			}
			children = parents;
			parents = new ArrayList<Node>();
			
		}
		outp.add(children.get(0));
		return outp;
		
	}
	public static String calculateHash(String message)
	{
		
		MessageDigest md;
		String currhash=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
			currhash = toHexString(hash);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return currhash;
	}
	
	public static String toHexString(byte[] hash)
	{
		BigInteger number = new BigInteger(1, hash);
		  
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
        while (hexString.length() < 32) 
        { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
	}
	public  List<Node> getMessage(List<Transactioncheck> t2)
	{
		List<String> messages = new ArrayList<String>();
		for (Transactioncheck t : t2)
		{
			messages.add(t.getSender()+t.getReceiver()+Double.toString(t.getAmount())+Double.toString(t.getGasfee()));
		}
		
		List<Node> dataNodes = new ArrayList<Node>();
		for(String d: messages)
		{
			dataNodes.add(new Node(null,null,calculateHash(d)));
			
		}
		int nsize = dataNodes.size();
		int i=0;
		while(nsize>(Math.pow(2,i)))
		{
			i+=1;
		}
		int t = (int) Math.pow(2, i);
		for(int j=nsize;j<t;j++)
		{
			dataNodes.add(new Node(null,null,null));
			
		}
		return dataNodes;
	}
	
	public  List<Node> getMessage1(List<Transactions> t2)
	{
		List<String> messages = new ArrayList<String>();
		for (Transactions t : t2)
		{
			messages.add(t.getSender()+t.getReceiver()+Double.toString(t.getAmount())+Double.toString(t.getGasfee()));
		}
		
		List<Node> dataNodes = new ArrayList<Node>();
		for(String d: messages)
		{
			dataNodes.add(new Node(null,null,calculateHash(d)));
			
		}
		int nsize = dataNodes.size();
		int i=0;
		while(nsize>(Math.pow(2,i)))
		{
			i+=1;
		}
		int t = (int) Math.pow(2, i);
		for(int j=nsize;j<t;j++)
		{
			dataNodes.add(new Node(null,null,null));
			
		}
		return dataNodes;
	}
	
	public  Long checkVerification(int id,List<Transactions> tcheck,List<Transactioncheck> t2)
	{
//		System.out.println(tcheck.get(1).getAmount()+"                "+t2.get(1).getAmount());
		List<Node> dataNodes1 = getMessage(t2);
		List<Node> out1 = buildTree(dataNodes1, 1);
		
		List<Node> dataNodes2 = getMessage1(tcheck);
		List<Node> out2 = buildTree(dataNodes2,1);
//		System.out.println(out1.size()+ " SIZE     "+out2.size());
		
		if(out1.size() != out2.size())
		{
			return 0L;
		}
		for(int i=0;i<out1.size()/2;i++)
		{
			Node  temp = out1.get(i);
			out1.set(i, out1.get(out1.size()-i-1));
			out1.set(out1.size()-i-1, temp);
		}
		for(int i=0;i<out2.size()/2;i++)
		{
			Node  temp = out2.get(i);
			out2.set(i, out2.get(out2.size()-i-1));
			out2.set(out2.size()-i-1, temp);
		}
		
		for(int i=0;i<out1.size();i++)
		{
			System.out.println(out1.get(i).getHash()+"                "+out1.get(i).getHash());
		}
		
		int siz = out1.size();
		
		
		int i=0;
		int d = siz-(int)(Math.pow(2, i));
		System.out.println(siz);
		while(d!=0)
		{

			siz=(int) (siz-Math.pow(2,i));
			i+=1;
			d = (int) (siz-Math.pow(2,i));
			
		}
		if(!out2.get(0).getHash().equals(out1.get(0).getHash()))
		{
			int l = 2*0+1;
			int r = 2*0+2;
			int g=0;
			while(l<out1.size()-siz)
			{
				int f=0;
			
				if(!out1.get(l).getHash().equals(out2.get(l).getHash()))
				{
				
					r = 2*l+2;
					
					l = 2*l+1;
					f=1;
					
				}
				else if(!out1.get(r).getHash().equals(out2.get(r).getHash()))
				{
					l = 2*r+1;
					r = 2*r+2;
					f=1;
				}
				if(f==0)
				{
					g=1;
					break;
				}
				
			}
			if(g==1)
			{
				return 0L;
			}
			else {
				if(!out1.get(l).getHash().equals(out2.get(l).getHash()))
				{
					return tcheck.get(out1.size()-l-1).getTransaction_id();
				}
				if(!out1.get(r).getHash().equals(out2.get(r).getHash()))
				{
					return tcheck.get(out1.size()-r-1).getTransaction_id();
				}
			
				
			}
		}
		return (long) 1;
		
	}
	
	
	
}
