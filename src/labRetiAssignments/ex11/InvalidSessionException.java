package labRetiAssignments.ex11;

public class InvalidSessionException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvalidSessionException()
	{
		super("Given session number for the given day is invalid.");
	}
}
