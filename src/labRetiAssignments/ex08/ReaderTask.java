package labRetiAssignments.ex08;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReaderTask implements Runnable
{
	private BankAccount account;
	private Map<BankMovementReason, AtomicInteger>  reasonCounters;
	
	public ReaderTask(BankAccount account, Map<BankMovementReason, AtomicInteger>  reasonCounters)
	{
		this.account = account;
		this.reasonCounters = reasonCounters;
	}

	@Override
	public void run()
	{
		for(BankMovement movement : account.movements)
		{
			AtomicInteger counter = reasonCounters.get(movement.reason);
			if(counter != null)
			{
				counter.incrementAndGet();
			}
			else
			{
				throw new IllegalStateException();
			}
		}
	}

}
