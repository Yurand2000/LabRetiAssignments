package labRetiAssignments.ex08;

import java.time.Instant;

public class BankMovement
{
	private BankMovementReason reason;
	private Instant timestamp;
	
	public BankMovement()
	{
		reason = BankMovementReason.InvalidReason;
		timestamp = Instant.EPOCH;
	}
	
	public BankMovement(BankMovementReason reason, Instant timestamp)
	{
		this.reason = reason;
		this.timestamp = timestamp;
	}
	
	public BankMovementReason getReason()
	{
		return reason;
	}
	
	public Instant getTimestamp()
	{
		return timestamp;
	}
}
