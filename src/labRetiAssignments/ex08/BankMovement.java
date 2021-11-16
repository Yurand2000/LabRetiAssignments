package labRetiAssignments.ex08;

import java.time.Instant;

public class BankMovement
{
	public final BankMovementReason reason;
	public final Instant timestamp;
	
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
	
	@Override
	public String toString()
	{
		return "{" + reason.toString() + "; " + timestamp.toString() + "}";
	}
}
