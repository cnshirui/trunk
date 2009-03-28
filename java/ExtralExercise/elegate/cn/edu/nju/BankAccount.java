package elegate.cn.edu.nju;

import java.text.NumberFormat;


/**
 * the main class
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
class Banking
{
	public static void main(String[] args)
	{
		BankAccount account=new BankAccount(0,1000,0.1);
		new Saver(account);
		new Spender(account);
	}
}



/**
 * class to store a account's information and perform operation on account
 * @author Elegate
 * @author cs department of NJU
 */
public class BankAccount
{
	/**
	 * the account's id
	 */
	private int id;
	/**
	 * the remaining money
	 */
	private double balance;
	/**
	 * the interest rate per year
	 */
	private double annualInterestRate;
	
	/**
	 * default constructor
	 *
	 */
	public BankAccount()
	{
		id=-1;
		balance=0;
	}
	/**
	 * constructor 
	 * @param id the account's id
	 * @param balance  the initial money saved
	 * @param annualInterestRate the interest rate per year
	 */
	public BankAccount(int id,double balance ,double annualInterestRate)
	{
		this.id=id;
		this.balance=balance;
		this.annualInterestRate=annualInterestRate;
	}
	/**
	 * Get the account's id
	 * @return the id of the account
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * Get the balance of the account
	 * @return the account's balance
	 */
	public synchronized double getBalance()
	{
		return this.balance;
	}
	
	/**
	 * Get the annual interest rate
	 * @return the annual interest rate
	 */
	public double getAnnualInterestRate()
	{
		return this.annualInterestRate;
	}
	/**
	 * Set the id of the account
	 * @param id the new id of the account
	 */
	public void setId(int id)
	{
		this.id=id;
	}
	
	/**
	 * Set the balance of the account
	 * @param balance the new balance of the account
	 */
	public synchronized void setBalance(double balance)
	{
		this.balance=balance;
	}
	
	/**
	 * Set the annual interest rate of the account
	 * @param annualInterestRate the new annual interest rate
	 * @throws Exception exception will be throwed when the argument is illegal
	 */
	public void setAnnualInterestRate
	(double annualInterestRate) throws Exception
	{
		if(annualInterestRate>=0)
		{
			this.annualInterestRate=annualInterestRate;
		}
		else
			throw new Exception("Illegal argument:"+annualInterestRate);
	}
	
	/**
	 * Get the monthly interest rate
	 * @return the monthly interest rate
	 */
	public double getMonthlyInterestRate()
	{
		return Math.pow(this.annualInterestRate+1,1.0/12)-1;
	}
	/**
	 * Withdraw some cash from the account
	 * @param amount the amount of cash will be withdrawed
	 */
	public synchronized boolean withdraw(double amount)
	{
		NumberFormat nf=NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		if(this.balance>=amount)
		{
			this.balance-=amount;
			System.out.println("Withdraw:"+	
					nf.format(amount)+"\t\tbalance="+nf.format(this.balance));		
			return true;
		}
		else
			System.out.println("No enough balance("+nf.format(this.balance)+"<"+nf.format(amount)+")");
		return false;
	}
	
	/**
	 * Deposit some money to the account
	 * @param amount the ammount of money to be deposited
	 * @throws Exception Exception will be throwed when the argument is illegal
	 */
	public synchronized boolean deposit(double amount)
	{
		if(amount>=0)
		{
			this.balance+=amount;
			NumberFormat nf=NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(3);
			System.out.println("Deposit:"
					+nf.format(amount)+"\t\tbalance="+nf.format(this.balance));		
			return true;
		}
		else
			return withdraw(-amount);
	}
	
	public String toString()
	{
		return this.getClass().getName()+":[id="+id
		+",balance="+balance+",annualInterestRate="+annualInterestRate+"]";
	}
}
/**
 * class to simulate a saver
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
class Saver extends Thread
{
	private BankAccount account;
	public Saver(BankAccount account)
	{
		this.account=account;
		this.start();
	}
	public void run()
	{
		while(true)
		{
			double amount=Math.random()*1000;
			account.deposit(amount);
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
/**
 * class to simulate a spender
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
class Spender extends Thread
{
	private BankAccount account;
	public Spender(BankAccount account)
	{
		this.account=account;
		this.start();
	}
	public void run()
	{
		while(true)
		{
			try
			{
				double amount=Math.random()*1000;
				while(!account.withdraw(amount))
				{
					Thread.sleep(1000);
				}
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}