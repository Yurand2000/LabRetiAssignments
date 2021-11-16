package labRetiAssignments.ex08;

import java.util.LinkedList;
import java.util.List;

public class BankAccount
{
	public final String account_holder;
	public final List<BankMovement> movements;
	
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
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("BankAccount: " + account_holder + ". Movements:\n* ");
		for(int i = 0; i < movements.size(); i++)
		{
			builder.append(movements.get(i));
			if(i != movements.size() - 1)
			{
				builder.append("\n* ");
			}
		}
		return builder.toString();
	}
}
