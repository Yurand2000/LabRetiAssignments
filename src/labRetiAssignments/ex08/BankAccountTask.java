package labRetiAssignments.ex08;

import java.util.Map;

public class BankAccountTask implements Runnable
{
	private BankAccount account;
	private Map<BankMovementReason, Integer> reasonCounters;
	
	public BankAccountTask(BankAccount account, Map<BankMovementReason, Integer> reasonCounters)
	{
		this.account = account;
		this.reasonCounters = reasonCounters;
	}

	@Override
	public void run()
	{
		for(BankMovement movement : account.movements)
		{
			if(reasonCounters.containsKey(movement.reason))
			{
				reasonCounters.put(movement.reason, reasonCounters.get(movement.reason) + 1);
			}
			else
			{
				reasonCounters.put(movement.reason, 1);
			}
		}
	}

}
