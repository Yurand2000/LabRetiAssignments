package labRetiAssignments.ex11;

import java.io.Serializable;

public class TestRemoteImpl implements TestRemoteInterface, Serializable
{
	private static final long serialVersionUID = 1L;
	private static int count = 0;
	private int id;
	
	public TestRemoteImpl()
	{
		id = count;
		count++;
	}
	
	@Override
	public String test()
	{
		System.out.println("remote call num: " + id);
		return "ciao mondo";
	}

}
