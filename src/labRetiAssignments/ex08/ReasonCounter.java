package labRetiAssignments.ex08;

public class ReasonCounter
{
	private final BankMovementReason reason;
	private int count;
	
	public ReasonCounter(BankMovementReason reason)
	{
		this.reason = reason;
		this.count = 0;
	}
	
	public BankMovementReason getReason()
	{
		return reason;
	}
	
	public synchronized int getCount()
	{
		return count;
	}
	
	public synchronized void increaseCount()
	{
		count++;
	}
}
