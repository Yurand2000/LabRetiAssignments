package labRetiAssignments.ex11;

public class MaxSpeakersException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public MaxSpeakersException()
	{
		super("Can't have more speakers added to the requested session.");
	}
}
