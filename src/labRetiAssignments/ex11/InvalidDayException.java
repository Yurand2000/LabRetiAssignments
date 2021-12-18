package labRetiAssignments.ex11;

public class InvalidDayException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidDayException()
	{
		super("Given day is invalid.");
	}	
}
