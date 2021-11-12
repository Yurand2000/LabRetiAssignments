package labRetiAssignments.ex08;

import java.util.LinkedList;
import java.util.List;

public class BankAccount
{
	private String account_holder;
	private List<BankMovement> movements;
	
	public BankAccount()
	{
		account_holder = new String();
		movements = new LinkedList<BankMovement>();
	}
	
	public BankAccount(String account_holder, List<BankMovement> movements)
	{
		this.account_holder = account_holder;
		this.movements = new LinkedList<BankMovement>(movements);
	}
	
	public String getAccountHolder()
	{
		return account_holder;
	}
	
	public List<BankMovement> getMovements()
	{
		return movements;
	}
}
